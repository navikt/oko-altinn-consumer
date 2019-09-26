//package no.nav.okonomi.altinn.consumer.formsubmitservice;
//
//import no.altinn.intermediaryinboundexternalec.FormTask;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
//@RunWith(MockitoJUnitRunner.class)
//public class FormTaskServiceTest {
//
//    private static final String ORDER_ID = "orderId";
//    private static final String ORGNUMMER = "orgnummer";
//    private static final String NAVN = "navn";
//    private static final String INNTEKTSAAR = "2017";
//    private static final byte[] VEDLEGG = {1, 2, 3};
//    private static final String SIDEN_DATO = "sidenDato";
//
//    private static final String SERVICE_CODE = "serviceCode";
//    private static final String SERVICE_EDITION_CODE = "1";
//    private static final String DATA_FORMAT_ID = "dataFormatId";
//    private static final String DATA_FORMAT_VERSION = "2";
//    private static final String LANGUAGE_ID = "3";
//    private static final String DATA_FORMAT_PROVIDER = "31";
//
//    @InjectMocks
//    private FormTaskService formTaskService;
//
//    @Before
//    public void setUp() {
//        formTaskService = new FormTaskService(new FormSubmitServiceProperties(SERVICE_CODE, SERVICE_EDITION_CODE, DATA_FORMAT_ID, DATA_FORMAT_VERSION));
//    }
//
//    @Test
//    public void createFormTask() {
//        FormTask formTask = formTaskService.createFormTask(stubMessage());
//        assertThat(formTask.getForms().getForm().size(), is(1));
//        assertThat(formTask.getServiceCode(), is(SERVICE_CODE));
//        assertThat(formTask.getServiceEdition(), is(Integer.parseInt(SERVICE_EDITION_CODE)));
//        assertThat(formTask.getForms().getForm().get(0).getDataFormatId(), is(DATA_FORMAT_ID));
//        assertThat(formTask.getForms().getForm().get(0).getDataFormatVersion(), is(Integer.parseInt(DATA_FORMAT_VERSION)));
//        assertThat(formTask.getForms().getForm().get(0).getEndUserSystemReference(), is(ORDER_ID));
//        assertThat(formTask.getForms().getForm().get(0).getParentReference(), is(0));
//    }
//
//    private RF1211MessageVO stubMessage() {
//        return new RF1211MessageBuilder()
//                .orderId(ORDER_ID)
//                .orgnummer(ORGNUMMER)
//                .navn(NAVN)
//                .inntektsaar(INNTEKTSAAR)
//                .vedlegg(VEDLEGG)
//                .skattekortVarslingstype(SkattekortVarslingstype.VARSEL_VED_FOERSTE_SKATTEKORTENDRING)
//                .skattekortMeldingstype(SkattekortMeldingstype.INNSENDING_MED_VEDLEGG_OG_EVT_ENDRINGER)
//                .skattekortHenteEndredeSkattekort(SkattekortHenteEndredeSkattekort.SIDEN_SISTE_FORESPOERSEL)
//                .sidenDato(SIDEN_DATO)
//                .build();
//    }
//
//}