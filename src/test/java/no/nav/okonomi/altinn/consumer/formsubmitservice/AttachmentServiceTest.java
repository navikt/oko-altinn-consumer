//package no.nav.okonomi.altinn.consumer.formsubmitservice;
//
//import no.altinn.intermediaryinboundexternalec.ArrayOfAttachment;
//import org.junit.Test;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
//
//public class AttachmentServiceTest {
//
//    private AttachmentService attachmentService = new AttachmentService();
//
//    @Test
//    public void createAttachment() throws Exception {
//        String orderId = "orderId";
//        byte[] byteArray = {1, 2, 3};
//        String mime = "xml";
//
//        ArrayOfAttachment arrayOfAttachment = attachmentService.createAttachment(orderId, byteArray, mime);
//
//        assertThat(arrayOfAttachment.getAttachment().size(), is(1));
//        assertThat(arrayOfAttachment.getAttachment().get(0).getAttachementData(), is(byteArray));
//        assertThat(arrayOfAttachment.getAttachment().get(0).getParentReference(), is(orderId));
//        assertThat(arrayOfAttachment.getAttachment().get(0).getEndUserSystemReference(), is(orderId));
//        assertThat(arrayOfAttachment.getAttachment().get(0).isEncrypted(), is(false));
//    }
//
//}