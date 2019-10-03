package no.nav.okonomi.altinn.consumer;

class AltinnConsumerException extends RuntimeException {

    private static final long serialVersionUID = -8132684987344552251L;

    AltinnConsumerException(String message){
        super(message);
    }
    AltinnConsumerException(String message, Throwable cause) {
        super(message, cause);
    }
}
