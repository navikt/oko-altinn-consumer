package no.nav.okonomi.altinn.consumer.interceptor;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.interceptor.Soap12FaultInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.tokenstore.TokenStoreUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Interceptor for å håndtere feil med context token.
 */
@SuppressWarnings("rawtypes")
public class BadContextTokenInFaultInterceptor extends AbstractPhaseInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BadContextTokenInFaultInterceptor.class);

    private static final String ERROR_CODE_BAD_CONTEXT_TOKEN = "BadContextToken";
    private static final String ERROR_CODE__INVALID_SECURITY = "InvalidSecurity";

    @SuppressWarnings("unchecked")
    public BadContextTokenInFaultInterceptor() {
        super(Phase.UNMARSHAL);
        getAfter().add(Soap12FaultInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) {
        Exception exception = message.getContent(Exception.class);
        if (exception instanceof SoapFault) {
            LOGGER.error("Server Gods not happy, sent you a soapFault.. Trying to recover..");
            SoapFault soapFault = (SoapFault) exception;
            List<QName> subCodes = soapFault.getSubCodes();
            for (QName subCode : subCodes) {
                LOGGER.error("Found subCode: {}", subCode.getLocalPart());
                if (subCode.getLocalPart().equalsIgnoreCase(ERROR_CODE_BAD_CONTEXT_TOKEN) || subCode.getLocalPart().equalsIgnoreCase(ERROR_CODE__INVALID_SECURITY)) {
                    String tokenId = (String) message.getContextualProperty(SecurityConstants.TOKEN_ID);
                    removeTokenFromMessageAndTokenStore(message, tokenId);
                    CookieStore.setCookie(null);
                    soapFault.setMessage("Token " + tokenId + " is removed from tokenstore, a new one will be requested on your next call. Message from server: " + soapFault.getMessage());
                    message.setContent(Exception.class, soapFault);
                }
            }
        }
    }

    private void removeTokenFromMessageAndTokenStore(Message message, String tokenId) {
        message.getExchange().getEndpoint().remove(SecurityConstants.TOKEN);
        message.getExchange().getEndpoint().remove(SecurityConstants.TOKEN_ID);
        message.getExchange().remove(SecurityConstants.TOKEN_ID);
        message.getExchange().remove(SecurityConstants.TOKEN);
        TokenStoreUtils.getTokenStore(message).remove(tokenId);
        LOGGER.error("Removed token {} from message and tokenStore", tokenId);
    }

}
