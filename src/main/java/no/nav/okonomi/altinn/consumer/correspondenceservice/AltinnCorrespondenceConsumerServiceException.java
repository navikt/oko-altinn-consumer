package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.nav.okonomi.altinn.consumer.AltinnConsumerServiceException;

public class AltinnCorrespondenceConsumerServiceException extends AltinnConsumerServiceException {
    private static final long serialVersionUID = 1932019549939051458L;

    public AltinnCorrespondenceConsumerServiceException(String message, Exception e) {
        super(message, e);
    }

    public AltinnCorrespondenceConsumerServiceException(String message, String faultReason, Integer faultCodeValue, Exception e) {
        super(message, faultReason, faultCodeValue, e);
    }
}