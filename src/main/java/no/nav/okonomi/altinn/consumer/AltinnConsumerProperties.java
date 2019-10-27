package no.nav.okonomi.altinn.consumer;

public interface AltinnConsumerProperties {

    String getSbsUserName();

    String getSbsPassword();

    String getCorrespondenceEndpointAddress();

    String getServiceCode();

    String getServiceEditionCode();

    String getDataFormatId();

    String getDataFormatVersion();

    String getLanguageId();

    String getIntermediaryInboundEndpointAddress();

    String getReceipEndpointAddress();

    String getVirksomhetUserName();

    String getVirksomhetPassord();

    String getAppcertKeystorealias();

    String getAppcertSecret();

    String getAppcertKeystoreFilePath();


}
