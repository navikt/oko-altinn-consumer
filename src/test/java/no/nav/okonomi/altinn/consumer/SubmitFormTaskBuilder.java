package no.nav.okonomi.altinn.consumer;

public class SubmitFormTaskBuilder {

    private Integer receiptId;
    private String externalShipmentReference;
    private String archiveReference;
    private String receiversReference;

    public SubmitFormTaskBuilder receipdId(Integer receiptId) {
        this.receiptId = receiptId;
        return this;
    }

    public SubmitFormTaskBuilder externalShipmentReference(String externalShipmentReference) {
        this.externalShipmentReference = externalShipmentReference;
        return this;
    }

    public SubmitFormTaskBuilder archiveReference(String archiveReference) {
        this.archiveReference = archiveReference;
        return this;
    }

    public SubmitFormTaskBuilder receiversReference(String receiversReference) {
        this.receiversReference = receiversReference;
        return this;
    }

    public SubmitFormTask build() {
        verifyNotNullValues();
        return new SubmitFormTask(receiptId, externalShipmentReference, archiveReference, receiversReference);
    }

    private void verifyNotNullValues() {
        if (receiptId == null || isEmpty(externalShipmentReference)) {
            throw new IllegalStateException("Not all required values given");
        }
    }

    private boolean isEmpty(String toTest){
        return "".equals(toTest) || toTest == null;
    }

}
