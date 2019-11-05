package no.nav.okonomi.altinn.consumer;


public class AltinnConsumerServiceException extends Exception {

    private static final long serialVersionUID = 1932019549939051458L;
    public static final Integer NO_FAULT_CODE = 0;
    public static final String NO_FAULT_REASON = "Ingen feilmelding fra Altinn";
    private final Integer faultCode;
    private final String faultReason;


    public AltinnConsumerServiceException(String message, String faultReason, Integer faultCode, Exception e) {
        super(message, e);
        this.faultCode =  faultCode != null ? faultCode : NO_FAULT_CODE;
        this.faultReason = faultReason != null ? faultReason : NO_FAULT_REASON;
    }

    public int getFaultCode() {
        return faultCode;
    }

    public String getFaultReason() {
        return faultReason;
    }



}
