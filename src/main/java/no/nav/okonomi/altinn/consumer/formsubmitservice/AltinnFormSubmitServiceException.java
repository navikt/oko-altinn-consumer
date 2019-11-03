package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.nav.okonomi.altinn.consumer.AltinnConsumerServiceException;

public class AltinnFormSubmitServiceException extends AltinnConsumerServiceException {

    private static final long serialVersionUID = -2626248481160079307L;

    public AltinnFormSubmitServiceException(String message) {
        super(message);
    }

    public AltinnFormSubmitServiceException(String message, Exception e) {
        super(message, e);
    }

    public AltinnFormSubmitServiceException(String message, String faultReason, Integer faultCodeValue, Exception e) {
        super(message, faultReason, faultCodeValue, e);
    }
}