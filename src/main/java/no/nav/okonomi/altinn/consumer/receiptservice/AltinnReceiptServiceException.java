package no.nav.okonomi.altinn.consumer.receiptservice;

public class AltinnReceiptServiceException extends RuntimeException {

    private static final long serialVersionUID = -5705266667418755441L;

    AltinnReceiptServiceException(String message, Exception e) {
        super(message + e.getMessage());
    }

}