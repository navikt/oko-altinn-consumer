package no.nav.okonomi.altinn.consumer;

public class TestPropertiesReader implements AltinnConsumerProperties {
    @Override
    public String getSbsUserName() {
        return "SbsUserName";
    }

    @Override
    public String getSbsPassword() {
        return "SbsPassword";
    }

    @Override
    public String getCorrespondenceEndpointAddress() {
        return "CorrespondenceEndpointAddress";
    }

    @Override
    public String getServiceCode() {
        return "ServiceCode";
    }

    @Override
    public String getServiceEditionCode() {
        return "ServiceEditionCod";
    }

    @Override
    public String getDataFormatId() {
        return "DataFormatId";
    }

    @Override
    public String getDataFormatVersion() {
        return "DataFormatVersion";
    }

    @Override
    public String getLanguageId() {
        return "1044";
    }

    @Override
    public String getIntermediaryInboundEndpointAddress() {
        return "IntermediaryInboundEndpointAddress";
    }

    @Override
    public String getReceipEndpointAddress() {
        return "ReceipEndpointAddress";
    }

    @Override
    public String getVirksomhetUserName() {
        return "VirksomhetUserName";
    }

    @Override
    public String getVirksomhetPassord() {
        return "VirksomhetPassord";
    }

    @Override
    public String getAppcertKeystorealias() {
        return "AppcertKeystorealias";
    }

    @Override
    public String getAppcertKeystoreType() {
        return "JKS";
    }

    @Override
    public String getAppcertSecret() {
        return "AppcertSecret";
    }

    @Override
    public String getAppcertKeystoreFilePath() {
        return "AppcertKeystoreFilePath";
    }
}
