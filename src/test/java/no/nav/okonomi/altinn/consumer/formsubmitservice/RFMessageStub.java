package no.nav.okonomi.altinn.consumer.formsubmitservice;

public final class RFMessageStub implements AltinnMessage {

    private final String orderId;
    private final String orgnummer;
    private final String formData;
    private final byte[] attachmentData;

    RFMessageStub(String orderId, String orgnummer, String formData, byte[] attachmentData) {
        this.orderId = orderId;
        this.orgnummer = orgnummer;
        this.formData = formData;
        this.attachmentData = attachmentData;

    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public String getOrgnummer() {
        return orgnummer;
    }

    @Override
    public String getFormData() {
        return formData;
    }

    @Override
    public byte[] getAttachmentData() {
        return attachmentData;
    }
}
