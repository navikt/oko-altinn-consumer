package no.nav.okonomi.altinn.consumer.correspondenceservice;

public class AltinnCorrespondenceServiceException extends RuntimeException {

    private static final long serialVersionUID = 1932019549939051458L;

    public AltinnCorrespondenceServiceException(String message, Exception e) {
        super(message + e.getMessage());
    }

}