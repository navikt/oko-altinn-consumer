package no.nav.okonomi.altinn.consumer.formsubmitservice;

public class AltinnFormSubmitServiceException extends RuntimeException {

    private static final long serialVersionUID = -2626248481160079307L;

    public AltinnFormSubmitServiceException(String message, Exception e) {
        super(message + e.getMessage());
    }

}