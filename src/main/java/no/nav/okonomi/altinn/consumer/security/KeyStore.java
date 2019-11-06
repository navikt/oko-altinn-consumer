package no.nav.okonomi.altinn.consumer.security;

public final class KeyStore {

    private final String keystorefile;
    private final String keystorealias;
    private final String secret;
    private final String type;

    public KeyStore(String keystorefile, String keystorealias, String secret, String type) {
        this.keystorefile = keystorefile;
        this.keystorealias = keystorealias;
        this.secret = secret;
        this.type = type;
    }

    public String getKeystorefile() {
        return keystorefile;
    }

    String getKeystorealias() {
        return keystorealias;
    }

    public String getSecret() {
        return secret;
    }

    public String getType() {
        return type;
    }
}
