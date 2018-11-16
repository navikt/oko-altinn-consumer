package no.nav.okonomi.altinn.consumer.formsubmitservice;

public class RF1211MessageVO implements AltinnMessage {

    /**
     * Bestillingsid, vår referanse i NAV
     */
    private String orderId;

    /**
     * RapporteringsenhetOrganisasjonsnummer registrert hos Altinn
     */
    private String orgnummer;

    /**
     * RapporteringsenhetNavn registrert hos Altinn
     */
    private String navn;

    /**
     * Hvilket inntektsår bestilling av skattekort gjelder for
     */
    private String inntektsaar;

    private byte[] byteMessage;

    /**
     * Her angir du om du ønsker varsling når en av de brukere endrer skattekortet sitt hos skatteetaten. Mulige valg er:
     * 1-Melding ved første endring av skattekort – kontaktpersonen vil få et varsel på sms og/eller e-post ved endring av
     * skattekort per år, og ved etterfølgende endringer så vil ikke kontaktpersonen lengre bli informert. 2-Ingen melding –
     * ingen informasjonsmelding fra Altinn blir sendt til kontaktpersonen.
     */
    private SkattekortVarslingstype skattekortVarslingstype;

    /**
     * Her kan velge mellom «Skattekort for alle brukere», «Skattekort for endringer og nybrukere», og «Skattekort for
     * endringer» Første valg er naturlig ved første forespørsel på nytt år, da man vil ha skattekort opplysninger på alle
     * brukere. De andre er naturlige under året, ved nykommende brukere eller om man rett og slett vil sjekke om det er
     * endringer siden forrige forespørsel.(Alle brukere sendes, men kun endringer kommer i retur.)
     */
    private SkattekortMeldingstype skattekortMeldingstype;


    /**
     * Valg for å hente endrede skattekort siden siste forespørsel eller en bestemt dato
     */
    private SkattekortHenteEndredeSkattekort skattekortHenteEndredeSkattekort;

    /**
     * Ikke nødvendig hvis man har valgt SidenSisteForespørsel for å hente endrede skattekort
     */
    private String sidenDato;


    RF1211MessageVO(String orderId,
                    String orgnummer,
                    String navn,
                    String inntektsaar,
                    byte[] byteMessage,
                    SkattekortVarslingstype skattekortVarslingstype,
                    SkattekortMeldingstype skattekortMeldingstype,
                    SkattekortHenteEndredeSkattekort skattekortHenteEndredeSkattekort,
                    String sidenDato) {

        this.orderId = orderId;
        this.byteMessage = byteMessage;
        this.orgnummer = orgnummer;
        this.navn = navn;
        this.skattekortVarslingstype = skattekortVarslingstype;
        this.skattekortMeldingstype = skattekortMeldingstype;
        this.inntektsaar = inntektsaar;
        this.skattekortHenteEndredeSkattekort = skattekortHenteEndredeSkattekort;
        this.sidenDato = sidenDato;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrgnummer() {
        return orgnummer;
    }

    @Override
    public String getFormData() {
        return null;
    }

    @Override
    public byte[] getAttachmentData() {
        return new byte[0];
    }

    String getNavn() {
        return navn;
    }

    SkattekortVarslingstype getSkattekortVarslingstype() {
        return skattekortVarslingstype;
    }

    SkattekortMeldingstype getSkattekortMeldingstype() {
        return skattekortMeldingstype;
    }

    String getInntektsaar() {
        return inntektsaar;
    }

    SkattekortHenteEndredeSkattekort getSkattekortHenteEndredeSkattekort() {
        return skattekortHenteEndredeSkattekort;
    }

    String getSidenDato() {
        return sidenDato;
    }

}