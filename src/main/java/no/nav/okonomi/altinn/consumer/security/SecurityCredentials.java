package no.nav.okonomi.altinn.consumer.security;

import java.util.Properties;

/**
 * Holder sikkerhetsinformasjon for tilgang til for en virksomhetsbruker.
 * <p>
 * <i>Immutable value object.</i>
 */
public class SecurityCredentials {

    private Properties keyStoreProperties;

    private String virksomhetsbruker;

    private String virksomhetsbrukerPassord;

    public SecurityCredentials() {
        super();
    }

    /**
     * Constructor.
     *
     * @param virksomhetsbruker        Brukernavn for virksomhetsbruker
     * @param virksomhetsbrukerPassord Passord for virksomhetsbruker
     */
    public SecurityCredentials(String virksomhetsbruker,
                               String virksomhetsbrukerPassord,
                               Properties keyStore) {
        this.virksomhetsbruker = virksomhetsbruker;
        this.virksomhetsbrukerPassord = virksomhetsbrukerPassord;
        keyStoreProperties = new Properties();
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.file", keyStore.getProperty("keystore"));
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", keyStore.getProperty("keystorepassword"));
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", "jks");
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.private.password", keyStore.getProperty("keystorepassword"));
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", keyStore.getProperty("keystorealias"));
    }

    /**
     * Constructor.
     *
     * @param virksomhetsbruker        Brukernavn for virksomhetsbruker
     * @param virksomhetsbrukerPassord Passord for virksomhetsbruker
     * @param securityFile             Applikasjons sertifikat (format PKCS12)
     * @param securityPassword         Passord tilhørende sertifikat
     * @param securityAlias            Alias tilhørende sertifikat (aka. friendly name)
     */
    public SecurityCredentials(String virksomhetsbruker,
                               String virksomhetsbrukerPassord,
                               String securityFile,
                               String securityPassword,
                               String securityAlias) {
        this.virksomhetsbruker = virksomhetsbruker;
        this.virksomhetsbrukerPassord = virksomhetsbrukerPassord;
        keyStoreProperties = new Properties();
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.file", securityFile);
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", securityPassword);
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", "pkcs12");
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.private.password", securityPassword);
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", securityAlias);
    }

    public Properties getKeyStoreProperties() {
        return keyStoreProperties;
    }

    public String getVirksomhetsbruker() {
        return virksomhetsbruker;
    }

    public String getVirksomhetsbrukerPassord() {
        return virksomhetsbrukerPassord;
    }

}
