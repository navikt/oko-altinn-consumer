//package no.nav.okonomi.altinn.consumer.formsubmitservice;
//
//import org.springframework.util.StringUtils;
//
//public class RF1211MessageBuilder {
//
//    private String orderId;
//    private String orgnummer;
//    private String navn;
//    private String inntektsaar;
//    private byte[] vedlegg;
//    private SkattekortVarslingstype skattekortVarslingstype;
//    private SkattekortMeldingstype skattekortMeldingstype;
//    private SkattekortHenteEndredeSkattekort skattekortHenteEndredeSkattekort;
//    private String sidenDato;
//
//    public RF1211MessageBuilder orderId(final String orderId) {
//        this.orderId = orderId;
//        return this;
//    }
//
//    public RF1211MessageBuilder orgnummer(final String orgnummer) {
//        this.orgnummer = orgnummer;
//        return this;
//    }
//
//    public RF1211MessageBuilder navn(final String navn) {
//        this.navn = navn;
//        return this;
//    }
//
//    public RF1211MessageBuilder inntektsaar(final String inntektsaar) {
//        this.inntektsaar = inntektsaar;
//        return this;
//    }
//
//    public RF1211MessageBuilder vedlegg(final byte[] vedlegg) {
//        this.vedlegg = vedlegg;
//        return this;
//    }
//
//    public RF1211MessageBuilder skattekortVarslingstype(SkattekortVarslingstype skattekortVarslingstype) {
//        this.skattekortVarslingstype = skattekortVarslingstype;
//        return this;
//    }
//
//    public RF1211MessageBuilder skattekortMeldingstype(SkattekortMeldingstype skattekortMeldingstype) {
//        this.skattekortMeldingstype = skattekortMeldingstype;
//        return this;
//    }
//
//    public RF1211MessageBuilder skattekortHenteEndredeSkattekort(SkattekortHenteEndredeSkattekort skattekortHenteEndredeSkattekort) {
//        this.skattekortHenteEndredeSkattekort = skattekortHenteEndredeSkattekort;
//        return this;
//    }
//
//    public RF1211MessageBuilder sidenDato(String sidenDato) {
//        this.sidenDato = sidenDato;
//        return this;
//    }
//
//    public RF1211MessageVO build() {
//        verifyNotNullValues();
//        return new RF1211MessageVO(
//                this.orderId,
//                this.orgnummer,
//                this.navn,
//                this.inntektsaar,
//                this.vedlegg,
//                this.skattekortVarslingstype,
//                this.skattekortMeldingstype,
//                this.skattekortHenteEndredeSkattekort,
//                this.sidenDato);
//    }
//
//    private void verifyNotNullValues() {
//        boolean stringValuesNotNull = StringUtils.isEmpty(orderId) ||
//                StringUtils.isEmpty(orgnummer) ||
//                StringUtils.isEmpty(navn) ||
//                StringUtils.isEmpty(inntektsaar);
//
//        boolean enumValuesNotNull = skattekortVarslingstype == null ||
//                skattekortMeldingstype == null;
//
//        if (stringValuesNotNull || enumValuesNotNull) {
//            throw new IllegalStateException("Not all required values given");
//        }
//    }
//
//}
