package no.nav.okonomi.altinn.consumer.formsubmitservice;

public final class FormSubmitServiceProperties {

    private final String serviceCode;
    private final String serviceEditionCode;
    private final String dataFormatId;
    private final String dataFormatVersion;
    private final String externalShipmentReferencePrefix;

    public FormSubmitServiceProperties(String serviceCode,
                                       String serviceEditionCode,
                                       String dataFormatId,
                                       String dataFormatVersion,
                                       String externalShipmentReferencePrefix) {
        this.serviceCode = serviceCode;
        this.serviceEditionCode = serviceEditionCode;
        this.dataFormatId = dataFormatId;
        this.dataFormatVersion = dataFormatVersion;
        this.externalShipmentReferencePrefix = externalShipmentReferencePrefix;
    }

    String getServiceCode() {
        return serviceCode;
    }

    String getServiceEditionCode() {
        return serviceEditionCode;
    }

    String getDataFormatId() {
        return dataFormatId;
    }

    String getDataFormatVersion() {
        return dataFormatVersion;
    }

    public String getExternalShipmentReferencePrefix() {
        return externalShipmentReferencePrefix;
    }
}
