package no.nav.okonomi.altinn.consumer.formsubmitservice;

public final class FormSubmitServiceProperties {

    private String serviceCode;
    private String serviceEditionCode;
    private String dataFormatId;
    private String dataFormatVersion;

    public FormSubmitServiceProperties(String serviceCode,
                                       String serviceEditionCode,
                                       String dataFormatId,
                                       String dataFormatVersion) {
        this.serviceCode = serviceCode;
        this.serviceEditionCode = serviceEditionCode;
        this.dataFormatId = dataFormatId;
        this.dataFormatVersion = dataFormatVersion;
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

}
