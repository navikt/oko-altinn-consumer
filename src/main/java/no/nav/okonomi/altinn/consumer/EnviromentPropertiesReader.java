package no.nav.okonomi.altinn.consumer;

import java.util.function.Function;
import java.util.function.Supplier;

public class EnviromentPropertiesReader implements AltinnConsumerProperties {

    public String getSbsUserName() {
        return getEnvVarOrThrow("altinn-consumer.srvuser-sbs.username");
    }

    public String getSbsPassword() {
        return getEnvVarOrThrow("altinn-consumer.srvuser-sbs.password");
    }

    public String getServiceCode() {
        return getEnvVarOrThrow("altinn-consumer.formsubmitservice.servicecode");
    }

    public String getServiceEditionCode() {
        return getEnvVarOrThrow("altinn-consumer.formsubmitservice.serviceeditioncode");
    }

    public String getDataFormatId() {
        return getEnvVarOrThrow("altinn-consumer.formsubmitservice.dataformatid");
    }

    public String getDataFormatVersion() {
        return getEnvVarOrThrow("altinn-consumer.formsubmitservice.dataformatversion");
    }

    public String getLanguageId() {
        return getEnvVarOrDefault("altinn-consumer.languageid", () -> "1044");
    }

    public String getCorrespondenceEndpointAddress() {
        return getEnvVarOrThrow("altinn-consumer.correspondence.url");
    }

    public String getIntermediaryInboundEndpointAddress() {
        return getEnvVarOrThrow("altinn-consumer.intermediaryinbound.url");
    }

    public String getReceipEndpointAddress() {
        return getEnvVarOrThrow("altinn-consumer.receipt.url");
    }


    public String getVirksomhetUserName() {
        return getEnvVarOrThrow("altinn-consumer.virksomhet.username");
    }

    public String getVirksomhetPassord() {
        return getEnvVarOrThrow("altinn-consumer.virksomhet.password");
    }

    public String getAppcertKeystorealias() {
        String modig = "no.nav.modig.security.appcert.keystorealias";
        String nais = "NAV_TRUSTSTORE_KEYSTOREALIAS";
        return getEnvVarOrDefault(modig, () -> getEnvVarOrDefault(nais, invalidProperty(modig, nais)));
    }

    public String getAppcertKeystoreType() {
        return getEnvVarOrDefault("altinn-consumer.security.appcert.keystoreType", () -> "JKS");
    }

    public String getAppcertSecret() {
        String modig = "no.nav.modig.security.appcert.password";
        String nais = "NAV_TRUSTSTORE_PASSWORD";
        return getEnvVarOrDefault(modig, () -> getEnvVarOrDefault(nais, invalidProperty(modig, nais)));
    }

    public String getAppcertKeystoreFilePath() {
        String modig = "no.nav.modig.security.appcert.keystore";
        String nais = "NAV_TRUSTSTORE_PATH";
        return getEnvVarOrDefault(modig, () -> getEnvVarOrDefault(nais, invalidProperty(modig, nais)));
    }

    private Supplier<String> invalidProperty(String... missingProperties) {
        return () -> {
            throw new AltinnConsumerException("Missing environment variable " + String.join(",", missingProperties));
        };
    }

    private Function<String, String> invalidProperty = x -> {
        throw new AltinnConsumerException("Missing environment variable " + x);
    };

    private String getEnvVarOrThrow(String key) {
        return getEnvVarOrDefault(key, () -> {
            throw new AltinnConsumerException("Missing environment variable " + key);
        });
    }

    private String getEnvVarOrDefault(String envVarQuery, Supplier<String> defaultSupplier) {
        String property = System.getProperty(envVarQuery);
        if (property != null) {
            return property;
        }
        String envVar = System.getenv(envVarQuery);
        if (envVar != null) {
            return envVar;
        }
        return defaultSupplier.get();
    }
}
