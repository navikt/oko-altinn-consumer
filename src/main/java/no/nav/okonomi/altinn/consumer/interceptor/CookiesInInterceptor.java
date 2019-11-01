package no.nav.okonomi.altinn.consumer.interceptor;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Interceptor for å plukke opp <i>Cookie</i> fra header i innkommende webservice melding.
 * Den vil da finnes som en attributt <i>Set-Cookie</i>. Hvis funnet så lagres den i {@link CookieStore}.
 */
@SuppressWarnings("rawtypes")
public class CookiesInInterceptor extends AbstractPhaseInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookiesInInterceptor.class);

    public CookiesInInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message message) throws Fault {
        Map<String, List> headers = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        List<Cookie> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            LOGGER.debug("CookiesInInterceptor -- cookie to be stored in cookiestore: {}", cookies.get(0));
            LOGGER.info("Setter NIET cookie");
        }
    }

}