package no.nav.okonomi.altinn.consumer;

public class AltinnConsumerInternalException extends Exception {

    public AltinnConsumerInternalException(String message) {
        super(message);
    }

    public AltinnConsumerInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}

