package no.nav.okonomi.altinn.consumer;

public class EnviromentPropertiesReader implements AltinnConsumerProperties {

    public String getSbsUserName() {
        return getEnvVarOrDefault("altinn-consumer.srvuser-sbs.username", null);
    }

    public String getSbsPassword() {
        return getEnvVarOrDefault("altinn-consumer.srvuser-sbs.password", null);
    }

    public String getServiceCode() {
        return getEnvVarOrDefault("altinn-consumer.formsubmitservice.servicecode", null);
    }

    public String getServiceEditionCode() {
        return getEnvVarOrDefault("altinn-consumer.formsubmitservice.serviceeditioncode", null);
    }

    public String getDataFormatId() {
        return getEnvVarOrDefault("altinn-consumer.formsubmitservice.dataformatid", null);
    }

    public String getDataFormatVersion() {
        return getEnvVarOrDefault("altinn-consumer.formsubmitservice.dataformatversion", null);
    }

    public String getLanguageId() {
        return getEnvVarOrDefault("altinn-consumer.languageid", "1044");
    }

    public String getCorrespondenceEndpointAddress() {
        return getEnvVarOrDefault("altinn-consumer.correspondence.url", null);
    }

    public String getIntermediaryInboundEndpointAddress() {
        return getEnvVarOrDefault("altinn-consumer.intermediaryinbound.url", null);
    }

    public String getReceipEndpointAddress() {
        return getEnvVarOrDefault("altinn-consumer.receipt.url", null);
    }


    public String getVirksomhetUserName() {
        return getEnvVarOrDefault("altinn-consumer.virksomhet.username", null);
    }

    public String getVirksomhetPassord() {
        return getEnvVarOrDefault("altinn-consumer.virksomhet.password", null);
    }

    public String getAppcertKeystorealias() {
        String alias = getEnvVarOrDefault("no.nav.modig.security.appcert.keystorealias", "NAIS");
        if(alias.equals("NAIS")){
            alias = getEnvVarOrDefault("NAV_TRUSTSTORE_KEYSTOREALIAS", null);
        }
        return alias;
    }

    public String getAppcertKeystoreType() {
        return getEnvVarOrDefault("altinn-consumer.security.appcert.keystoreType", "JKS");
    }

    public String getAppcertSecret() {
        String secret = getEnvVarOrDefault("no.nav.modig.security.appcert.password", "NAIS");
        if(secret.equals("NAIS")){
            secret = getEnvVarOrDefault("NAV_TRUSTSTORE_PASSWORD", null);
        }
        return secret;
    }

    public String getAppcertKeystoreFilePath() {
        String keystoreFile = getEnvVarOrDefault("no.nav.modig.security.appcert.keystore", "NAIS");
         if(keystoreFile.equals("NAIS")){
             keystoreFile = getEnvVarOrDefault("NAV_TRUSTSTORE_PATH", null);
         }
        return keystoreFile;
    }

    private String getEnvVarOrDefault(String envVarQuery, String defaultValue) {
        String property = System.getProperty(envVarQuery);
        if (property != null){
            return property;
        }
        String envVar = System.getenv(envVarQuery);
        if (envVar != null){
            return envVar;
        }
        if(defaultValue != null){
        return defaultValue;
        }
        throw new AltinnConsumerException("Missing environment variable " + envVarQuery);
    }
}
