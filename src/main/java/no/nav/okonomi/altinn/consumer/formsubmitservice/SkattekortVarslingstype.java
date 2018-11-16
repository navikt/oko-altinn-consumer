package no.nav.okonomi.altinn.consumer.formsubmitservice;

public enum SkattekortVarslingstype {
    VARSEL_VED_FOERSTE_SKATTEKORTENDRING(1),
    INGEN_VARSEL(3);

    private int skattekortVarslingstypeVal;

    SkattekortVarslingstype(int skattekortVarslingstypeVal) {
        this.skattekortVarslingstypeVal = skattekortVarslingstypeVal;
    }

    public int getVarslingstype() {
        return skattekortVarslingstypeVal;
    }

}
