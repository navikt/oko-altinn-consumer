package no.nav.okonomi.altinn.consumer;

public interface AltinnConsumerProperties {

    String getSbsUserName();

    String getSbsPassword();

    String getServiceCode();

    String getServiceEditionCode();

    String getDataFormatId();

    String getDataFormatVersion();

    String getLanguageId();

    String getCorrespondenceEndpointAddress();

    String getIntermediaryInboundEndpointAddress();

    String getReceipEndpointAddress();

    String getVirksomhetUserName();

    String getVirksomhetPassord();

    String getAppcertKeystorealias();

    String getAppcertKeystoreType();

    String getAppcertSecret();

    String getAppcertKeystoreFilePath();

    String getExternalShipmentReferencePrefix();
}
