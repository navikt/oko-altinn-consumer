package no.nav.okonomi.altinn.consumer.receiptservice;

import no.nav.okonomi.altinn.consumer.AltinnConsumerServiceException;

public class AltinnReceiptServiceException extends AltinnConsumerServiceException {

    private static final long serialVersionUID = -5705266667418755441L;

    public AltinnReceiptServiceException(String message) {
        super(message);
    }

    public AltinnReceiptServiceException(String message, Exception e) {
        super(message, e);
    }

    public AltinnReceiptServiceException(String message, String faultReason, Integer faultCodeValue, Exception e) {
        super(message, faultReason, faultCodeValue, e);
    }
}