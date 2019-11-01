package no.nav.okonomi.altinn.consumer;

public final class SubmitFormTask {

    /**
     * KvitteringsId, referanse fra Altinn
     */
    private Integer receiptId;

    private String archiveReference;

    private String receiversReference;

    /**
     * Bestillingsid i AltinnMessage, vår referanse i NAV
     */
    private String externalShipmentReference;

    public SubmitFormTask(Integer receiptId,
                          String externalShipmentReference,
                          String archiveReference,
                          String receiversReference) {
        this.receiptId = receiptId;
        this.externalShipmentReference = externalShipmentReference;
        this.archiveReference = archiveReference;
        this.receiversReference = receiversReference;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public String getExternalShipmentReference() {
        return externalShipmentReference;
    }

    public String getArchiveReference() {
        return archiveReference;
    }

    public String getReceiversReference() {
        return receiversReference;
    }

}
