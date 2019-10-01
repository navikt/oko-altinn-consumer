package no.nav.okonomi.altinn.consumer;

import no.nav.okonomi.altinn.consumer.interceptor.BadContextTokenInFaultInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesInInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.CookiesOutInterceptor;
import no.nav.okonomi.altinn.consumer.interceptor.HeaderInterceptor;
import no.nav.okonomi.altinn.consumer.security.KeyStore;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.security.auth.callback.CallbackHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class AbstractConfig {

    private final SpringBusFactory busFactory = new SpringBusFactory();

    protected void setRequestContext(Client client, SecurityCredentials credentials) {
        client.getRequestContext().put("security.must-understand", Boolean.TRUE);
        client.getRequestContext().put("org.apache.cxf.message.Message.MAINTAIN_SESSION", Boolean.TRUE);
        client.getRequestContext().put("javax.xml.ws.session.maintain", Boolean.TRUE);
        client.getRequestContext().put("security.cache.issued.token.in.endpoint", Boolean.TRUE); // default: true
        client.getRequestContext().put("security.issue.after.failed.renew", Boolean.TRUE); // This must be set to true (default: true)
        client.getRequestContext().put("security.signature.properties", credentials.getKeyStoreProperties());
    }

    @SuppressWarnings("unchecked")
    protected Bus createBus() {
        Bus bus = busFactory.createBus();
   //     LoggingFeature loggingFeature = new LoggingFeature();
   //     loggingFeature.setPrettyLogging(true);
   //     loggingFeature.initialize(bus);
   //     bus.getFeatures().add(loggingFeature);
        bus.getInInterceptors().add(new CookiesInInterceptor());
        bus.getOutInterceptors().add(new CookiesOutInterceptor());
        bus.getOutInterceptors().add(new HeaderInterceptor());
        bus.getInFaultInterceptors().add(new BadContextTokenInFaultInterceptor());

        return bus;
    }

    protected Map<String, Object> cryptoProperties(SecurityCredentials securityCredentials) {
        KeyStore keyStore = securityCredentials.getKeyStore();
        Properties signingCryptProperties = new Properties();
        signingCryptProperties.put("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.password", keyStore.getSecret());
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.file", keyStore.getKeystorefile());
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.type", "JKS");
        signingCryptProperties.put("org.apache.ws.security.crypto.merlin.keystore.private.password", keyStore.getSecret());
        Map<String, Object> properties = new HashMap<>();
        properties.put("security.signature.properties.sct", signingCryptProperties);
        return properties;
    }

    protected WSS4JOutInterceptor wss4JOutInterceptor(String userName, CallbackHandler clientCallBackHandler) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.put(WSHandlerConstants.USER, userName);
        properties.put(WSHandlerConstants.PW_CALLBACK_REF, clientCallBackHandler);

        return new WSS4JOutInterceptor(properties);
    }

}
