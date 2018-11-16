package no.nav.okonomi.altinn.consumer.formsubmitservice;

public enum SkattekortHenteEndredeSkattekort {
    SIDEN_SISTE_FORESPOERSEL("SidenSisteForespørsel"),
    SIDEN_DATO("SidenDato");

    private String skattekortHenteEndredeSkattekort;

    SkattekortHenteEndredeSkattekort(String skattekortHenteEndredeSkattekort) {
        this.skattekortHenteEndredeSkattekort = skattekortHenteEndredeSkattekort;
    }

    public String getHenteEndredeSkattekort() {
        return skattekortHenteEndredeSkattekort;
    }

}
