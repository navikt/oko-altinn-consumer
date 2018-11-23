package no.nav.okonomi.altinn.consumer.security;

public final class KeyStore {

    private String keystorefile;
    private String keystorealias;
    private String secret;
    private String type;

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
