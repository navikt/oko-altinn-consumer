package no.nav.okonomi.altinn.consumer;


public class AltinnConsumerServiceException extends Exception {

    private static final long serialVersionUID = 1932019549939051458L;
    public static final Integer INTERNAL_OR_NO_FAULT = 0;
    public static final String INTERNAL_FAULT_REASON="Intern feil";
    public static final String NO_FAULT_REASON = "Ingen feilmelding fra Altinn";
    private final FaultCode faultCode;
    private final Integer faultCodeValue;
    private final String faultReason;

    public enum FaultCode{
        NO_FAULT,
        INTERNAL_FAULT,
        ALTINN_FAULT
    }

    public AltinnConsumerServiceException(String message) {
        super(message);
        this.faultCode = FaultCode.INTERNAL_FAULT;
        this.faultCodeValue = INTERNAL_OR_NO_FAULT;
        this.faultReason = INTERNAL_FAULT_REASON;
    }

    public AltinnConsumerServiceException(String message, Exception e) {
        super(message, e);
        this.faultCode = FaultCode.INTERNAL_FAULT;
        this.faultCodeValue = INTERNAL_OR_NO_FAULT;
        this.faultReason = INTERNAL_FAULT_REASON;
    }

    public AltinnConsumerServiceException(String message, String faultReason, Integer faultCodeValue, Exception e) {
        super(message, e);
        if(faultCodeValue != null){
            this.faultCode = FaultCode.ALTINN_FAULT;
            this.faultCodeValue = faultCodeValue;
        }else{
            this.faultCode = FaultCode.NO_FAULT;
            this.faultCodeValue = INTERNAL_OR_NO_FAULT;
        }
        this.faultReason = faultReason != null ? faultReason : NO_FAULT_REASON;
    }

    public int getFaultCodeValue() {
        return faultCodeValue;
    }

    public String getFaultReason() {
        return faultReason;
    }

    public FaultCode getFaultCode(){
        return faultCode;
    }


}
