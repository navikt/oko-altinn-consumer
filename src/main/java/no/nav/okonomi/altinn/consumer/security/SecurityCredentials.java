package no.nav.okonomi.altinn.consumer.security;

import java.util.Properties;

/**
 * Holder sikkerhetsinformasjon for tilgang til for en virksomhetsbruker.
 * <p>
 * <i>Immutable value object.</i>
 */
public class SecurityCredentials {

    private KeyStore keyStore;

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
                               KeyStore keyStore) {
        this.virksomhetsbruker = virksomhetsbruker;
        this.virksomhetsbrukerPassord = virksomhetsbrukerPassord;
        this.keyStore = keyStore;
        keyStoreProperties = new Properties();
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.file", keyStore.getKeystorefile());
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", keyStore.getSecret());
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", keyStore.getType());
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.private.password", keyStore.getSecret());
        keyStoreProperties.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", keyStore.getKeystorealias());
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
        this(virksomhetsbruker, virksomhetsbrukerPassord,
                new KeyStore(securityFile, securityPassword, securityAlias, "pkcs12"));
    }

    public KeyStore getKeyStore() {
        return keyStore;
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
