package no.nav.okonomi.altinn.consumer.formsubmitservice;

public enum SkattekortMeldingstype {
    INNSENDING_MED_VEDLEGG_UTEN_ENDRINGER(1),
    INNSENDING_MED_VEDLEGG_OG_EVT_ENDRINGER(2),
    INNSENDING_KUN_ENDRINGER(3);

    private int skattekortMeldingstype;

    SkattekortMeldingstype(int skattekortMeldingstype) {
        this.skattekortMeldingstype = skattekortMeldingstype;
    }

    public int getMeldingstype() {
        return skattekortMeldingstype;
    }

}
