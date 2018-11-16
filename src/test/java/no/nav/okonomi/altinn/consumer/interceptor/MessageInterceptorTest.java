package no.nav.okonomi.altinn.consumer.interceptor;

import org.apache.cxf.message.ExchangeImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.ZonedDateTime;

@RunWith(MockitoJUnitRunner.class)
public class MessageInterceptorTest {

    @InjectMocks
    private MessageInterceptor messageInterceptor;
    @Test
    public void handleMessage() throws Exception {
        Message  message = new MessageImpl();
        message.setExchange(new ExchangeImpl());
        message.put(Message.CONTENT_TYPE, "application/xml");
        message.setContent(InputStream.class, new ByteArrayInputStream(("<s:Envelope xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "<s:Header>\n" +
                "    <a:Action s:mustUnderstand=\"1\">http://www.altinn.no/services/Intermediary/Shipment/IntermediaryInbound/2010/10/IIntermediaryInboundExternalEC/SubmitFormTaskECResponse</a:Action>\n" +
                "    <a:RelatesTo>urn:uuid:db2505e8-3fe2-49c1-b252-89185e8cebc6</a:RelatesTo>\n" +
                "    <o:Security xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" s:mustUnderstand=\"1\">\n" +
                "      <u:Timestamp u:Id=\"_0\">\n" +
                "        <u:Created>2017-11-29T17:07:12.642Z</u:Created>\n" +
                "        <u:Expires>2017-11-29T17:12:12.642Z</u:Expires>\n" +
                "      </u:Timestamp>\n" +
                "    </o:Security>\n" +
                "  </s:Header>\n" +
                "  <s:Body xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <SubmitFormTaskECResponse xmlns=\"http://www.altinn.no/services/Intermediary/Shipment/IntermediaryInbound/2010/10\">\n" +
                "      <SubmitFormTaskECResult>\n" +
                "        <ReceiptId>13565444</ReceiptId>\n" +
                "        <ReceiptText>FormTaskShipment Data is sent to FormTask for Instantiating</ReceiptText>\n" +
                "        <ReceiptHistory/>\n" +
                "        <LastChanged>2017-11-29T18:07:12.463</LastChanged>\n" +
                "        <ReceiptTypeName>FormTask</ReceiptTypeName>\n" +
                "        <ReceiptStatusCode>OK</ReceiptStatusCode>\n" +
                "        <ParentReceiptId>0</ParentReceiptId>\n" +
                "        <References>\n" +
                "          <ReferenceBE>\n" +
                "            <ReferenceValue>1110</ReferenceValue>\n" +
                "            <ReferenceTypeName>ExternalShipmentReference</ReferenceTypeName>\n" +
                "            <ReporteeID>0</ReporteeID>\n" +
                "          </ReferenceBE>\n" +
                "          <ReferenceBE>\n" +
                "            <ReferenceValue>910962728</ReferenceValue>\n" +
                "            <ReferenceTypeName>OwnerPartyReference</ReferenceTypeName>\n" +
                "            <ReporteeID>0</ReporteeID>\n" +
                "          </ReferenceBE>\n" +
                "          <ReferenceBE>\n" +
                "            <ReferenceValue>974761076</ReferenceValue>\n" +
                "            <ReferenceTypeName>PartyReference</ReferenceTypeName>\n" +
                "            <ReporteeID>0</ReporteeID>\n" +
                "          </ReferenceBE>\n" +
                "        </References>\n" +
                "      </SubmitFormTaskECResult>\n" +
                "    </SubmitFormTaskECResponse>\n" +
                "  </s:Body></s:Envelope>").getBytes()));

        messageInterceptor.handleMessage(message);
        ZonedDateTime a = CookieStore.getRequestCookieTimeout();
        Assert.assertNotNull(a);
        Assert.assertEquals(ZonedDateTime.parse("2017-11-29T17:12:12.642Z"), a);
    }
}
