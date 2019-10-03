package no.nav.okonomi.altinn.consumer;

public class EnviromentPropertiesReader implements AltinnConsumerProperties {

    public String getUserName() {
        return assertNotNull("altinn-consumer.srvuser-sbs.username");
    }

    public String getCorrespondenceEndpointAddress() {
        return assertNotNull("altinn-consumer.correspondence.url");
    }

    public String getRf1211ServiceCode() {
        return getResultOrDefault("altinn-consumer.formsubmitservice.servicecode", "3103");
    }

    public String getRf1211ServiceEditionCode() {
        return getResultOrDefault("altinn-consumer.formsubmitservice.serviceeditioncode","170424");
    }

    public String getRf1211DataFormatId() {
        return getResultOrDefault("altinn-consumer.formsubmitservice.dataformatid","1548");
    }

    public String getRf1211DataFormatVersion() {
        return getResultOrDefault("altinn-consumer.formsubmitservice.dataformatversion","11936");
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

    public String getAppcertSecret() {
        return assertNotNull("altinn-consumer.security.appcert.password");
    }

    public String getAppcertKeystore() {
        return getResultOrDefault("altinn-consumer.security.appcert.keystore","app-key");
    }

    public String getLanguageId() {
        return getResultOrDefault("nav.altinn-consumer.languageid","1044");
    }

    public String getPassword() {
        return assertNotNull("altinn-consumer.srvuser-sbs.password");
    }

    private String assertNotNull(String envVarQuery){
        String envVarResult = System.getenv(envVarQuery);
        if(envVarResult == null) throw new AltinnConsumerException("Missing environment variable " + envVarQuery);
        return envVarResult;
    }

    private String getResultOrDefault(String envVarQuery, String defaultValue){
        String envVarResult = System.getenv(envVarQuery);
        return envVarResult != null ? envVarResult : defaultValue;
    }
}
