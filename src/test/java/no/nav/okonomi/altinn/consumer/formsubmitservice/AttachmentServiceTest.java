package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfAttachment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AttachmentServiceTest {

    private AttachmentService attachmentService = new AttachmentService();

    @Test
    public void createAttachment() throws Exception {
        String orderId = "orderId";
        byte[] byteArray = {1, 2, 3};
        String mime = "xml";

        ArrayOfAttachment arrayOfAttachment = attachmentService.createAttachment(orderId, byteArray, mime);

        assertEquals(1, arrayOfAttachment.getAttachment().size());
        assertEquals(byteArray, arrayOfAttachment.getAttachment().get(0).getAttachementData());
        assertEquals(orderId, arrayOfAttachment.getAttachment().get(0).getParentReference());
        assertEquals(orderId, arrayOfAttachment.getAttachment().get(0).getEndUserSystemReference());
        assertEquals(false, arrayOfAttachment.getAttachment().get(0).isEncrypted());
    }

}