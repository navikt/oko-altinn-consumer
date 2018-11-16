package no.nav.okonomi.altinn.consumer.formsubmitservice;

public interface AltinnMessage {

    String getOrderId();

    String getOrgnummer();

    String getFormData();

    byte[] getAttachmentData();

}
