package no.nav.okonomi.altinn.consumer;

public class EnviromentPropertiesReader implements AltinnConsumerProperties {

    public String getSbsUserName() {
        return getEnvVar("altinn-consumer.srvuser-sbs.username");
    }

    public String getSbsPassword() {
        return getEnvVar("altinn-consumer.srvuser-sbs.password");
    }

    public String getServiceCode() {
        return getEnvVar("altinn-consumer.formsubmitservice.servicecode");
    }

    public String getServiceEditionCode() {
        return getEnvVar("altinn-consumer.formsubmitservice.serviceeditioncode");
    }

    public String getDataFormatId() {
        return getEnvVar("altinn-consumer.formsubmitservice.dataformatid");
    }

    public String getDataFormatVersion() {
        return getEnvVar("altinn-consumer.formsubmitservice.dataformatversion");
    }

    public String getLanguageId() {
        return getEnvVarOrDefault("altinn-consumer.languageid", "1044");
    }

    public String getCorrespondenceEndpointAddress() {
        return getEnvVar("altinn-consumer.correspondence.url");
    }

    public String getIntermediaryInboundEndpointAddress() {
        return getEnvVar("altinn-consumer.intermediaryinbound.url");
    }

    public String getReceipEndpointAddress() {
        return getEnvVar("altinn-consumer.receipt.url");
    }


    public String getVirksomhetUserName() {
        return getEnvVar("altinn-consumer.virksomhet.username");
    }

    public String getVirksomhetPassord() {
        return getEnvVar("altinn-consumer.virksomhet.password");
    }

    public String getAppcertKeystorealias() {
        return getEnvVar("altinn-consumer.security.appcert.keystorealias");
    }

    public String getAppcertKeystoreType() {
        return getEnvVarOrDefault("altinn-consumer.security.appcert.keystoreType", "JKS");
    }

    public String getAppcertSecret() {
        return getEnvVar("altinn-consumer.security.appcert.password");
    }

    public String getAppcertKeystoreFilePath() {
        return getEnvVar("altinn-consumer.security.appcert.keystore");
    }

    private String getEnvVar(String envVarQuery) {
        String envVarResult = System.getenv(envVarQuery);
        if (envVarResult == null) throw new AltinnConsumerException("Missing environment variable " + envVarQuery);
        return envVarResult;
    }

    private String getEnvVarOrDefault(String envVarQuery, String defaultValue) {
        String envVarResult = System.getenv(envVarQuery);
        return envVarResult != null ? envVarResult : defaultValue;
    }
}
