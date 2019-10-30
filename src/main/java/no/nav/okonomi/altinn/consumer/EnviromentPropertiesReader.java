package no.nav.okonomi.altinn.consumer;

public class EnviromentPropertiesReader implements AltinnConsumerProperties {

    public String getSbsUserName() {
        return assertNotNull("altinn-consumer.srvuser-sbs.username");
    }

    public String getSbsPassword() {
        return assertNotNull("altinn-consumer.srvuser-sbs.password");
    }

    public String getCorrespondenceEndpointAddress() {
        return assertNotNull("altinn-consumer.correspondence.url");
    }

    public String getServiceCode() {
        return assertNotNull("altinn-consumer.formsubmitservice.servicecode");
    }

    public String getServiceEditionCode() {
        return assertNotNull("altinn-consumer.formsubmitservice.serviceeditioncode");
    }

    public String getDataFormatId() {
        return assertNotNull("altinn-consumer.formsubmitservice.dataformatid");
    }

    public String getDataFormatVersion() {
        return assertNotNull("altinn-consumer.formsubmitservice.dataformatversion");
    }

    public String getLanguageId() {
        return getResultOrDefault("nav.altinn-consumer.languageid", "1044");
    }

    public String getIntermediaryInboundEndpointAddress() {
        return assertNotNull("altinn-consumer.intermediaryinbound.url");
    }

    public String getReceipEndpointAddress() {
        return assertNotNull("altinn-consumer.receipt.url");
    }


    public String getVirksomhetUserName() {
        return assertNotNull("altinn-consumer.virksomhet.username");
    }

    public String getVirksomhetPassord() {
        return assertNotNull("altinn-consumer.virksomhet.password");
    }

    public String getAppcertKeystorealias() {
        return assertNotNull("altinn-consumer.security.appcert.keystorealias");
    }

    public String getAppcertKeystoreType() {
        return getResultOrDefault("altinn-consumer.security.appcert.keystoreType", "JKS");
    }

    public String getAppcertSecret() {
        return assertNotNull("altinn-consumer.security.appcert.password");
    }

    public String getAppcertKeystoreFilePath() {
        return assertNotNull("altinn-consumer.security.appcert.keystore");
    }

    private String assertNotNull(String envVarQuery) {
        String envVarResult = System.getenv(envVarQuery);
        if (envVarResult == null) throw new AltinnConsumerException("Missing environment variable " + envVarQuery);
        return envVarResult;
    }

    private String getResultOrDefault(String envVarQuery, String defaultValue) {
        String envVarResult = System.getenv(envVarQuery);
        return envVarResult != null ? envVarResult : defaultValue;
    }
}
