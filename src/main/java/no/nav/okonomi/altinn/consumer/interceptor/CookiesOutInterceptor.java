package no.nav.okonomi.altinn.consumer.interceptor;


import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Interceptor for å hente <i>Cookie</i> fra {@link CookieStore} og legge til i header i utgående webservice melding.
 */
@SuppressWarnings("rawtypes")
public class CookiesOutInterceptor extends AbstractPhaseInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookiesOutInterceptor.class);
    private static final ZoneId UTC = ZoneId.of("UTC");

    public CookiesOutInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message message) throws Fault {
        Map<String, List> headers = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        if (CookieStore.getRequestCookieTimeout() != null
                && CookieStore.getRequestCookieTimeout().minusSeconds(30).isBefore(ZonedDateTime.now(UTC))) {
            LOGGER.info("Token utløper {} (innen 30 sek). Nullstiller Token.", CookieStore.getRequestCookieTimeout());
            CookieStore.setCookie(null);
            CookieStore.setRequestCookieTimeout(null);
        }
        if (CookieStore.getCookie() != null) {
            LOGGER.debug("CookiesOUTInterceptor -- cookie to attach to header: {}", CookieStore.getCookie());
            headers.put("Cookie", Collections.singletonList(CookieStore.getCookie()));
        }
    }

}