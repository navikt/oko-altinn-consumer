package no.nav.okonomi.altinn.consumer.interceptor;

import com.google.common.base.Charsets;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class MessageInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageInterceptor.class);

    public MessageInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        message.put(Message.ENCODING, "UTF-8");
        InputStream inputStream = message.getContent(InputStream.class);
        if (inputStream != null) {
            CachedOutputStream outputStream = new CachedOutputStream();
            try {
                IOUtils.copy(inputStream, outputStream);
                String soapMessage = new String(outputStream.getBytes(), Charsets.UTF_8);
                LOGGER.debug("-------------------------------------------");
                LOGGER.debug("Incoming message is {}", soapMessage);
                LOGGER.debug("-------------------------------------------");
                outputStream.flush();
                message.setContent(InputStream.class, inputStream);

                inputStream.close();
                message.setContent(InputStream.class, new ByteArrayInputStream(soapMessage.getBytes(Charsets.UTF_8)));
                ZonedDateTime tokenExpireDateTime = finnTokenTimeoutVerdi(soapMessage);
                if (tokenExpireDateTime != null) {
                    LOGGER.debug("Mottatt tokenExpiredDate {}. Lagrer i trådens minne", tokenExpireDateTime);
                    CookieStore.setRequestCookieTimeout(tokenExpireDateTime);
                }
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private ZonedDateTime finnTokenTimeoutVerdi(String soapMessage) {

        XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            String expression = "/*[local-name()='Envelope']" +
                    "/*[local-name()='Header']" +
                    "/*[local-name()='Security']" +
                    "/*[local-name()='Timestamp']" +
                    "/*[local-name()='Expires']/text()";
            InputSource source = new InputSource(new StringReader(soapMessage));
            String timestamp = (String) xPath.compile(expression).evaluate(source, XPathConstants.STRING);
            return ZonedDateTime.parse(timestamp);
        } catch (XPathExpressionException | DateTimeParseException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
