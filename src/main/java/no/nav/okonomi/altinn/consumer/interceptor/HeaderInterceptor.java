package no.nav.okonomi.altinn.consumer.interceptor;

import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Interceptor for å legge til attributten <i>Connection</i> med verdi <i>Keep-Alive</i>
 * i header på utgående webservice melding.
 */
@SuppressWarnings("rawtypes")
public class HeaderInterceptor extends AbstractPhaseInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderInterceptor.class);

    @SuppressWarnings("unchecked")
    public HeaderInterceptor() {
        super(Phase.PRE_PROTOCOL_ENDING);
        getAfter().add(SAAJOutInterceptor.SAAJOutEndingInterceptor.class.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message message) {
        LOGGER.debug("Adding Keep-Alive header");
        Map<String, List> headers = (Map<String, List>) message.get(Message.PROTOCOL_HEADERS);
        headers.put("Connection", Collections.singletonList("Keep-Alive"));
    }

}