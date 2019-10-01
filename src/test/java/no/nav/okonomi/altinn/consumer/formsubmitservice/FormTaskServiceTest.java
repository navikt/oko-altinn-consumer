package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.FormTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FormTaskServiceTest {

    private static final String ORDER_ID = "orderId";
    private static final String ORGNUMMER = "orgnummer";
    private static final String NAVN = "navn";
    private static final String INNTEKTSAAR = "2017";
    private static final byte[] VEDLEGG = {1, 2, 3};
    private static final String SIDEN_DATO = "sidenDato";

    private static final String SERVICE_CODE = "serviceCode";
    private static final String SERVICE_EDITION_CODE = "1";
    private static final String DATA_FORMAT_ID = "dataFormatId";
    private static final String DATA_FORMAT_VERSION = "2";
    private static final String LANGUAGE_ID = "3";
    private static final String DATA_FORMAT_PROVIDER = "31";

    @InjectMocks
    private FormTaskService formTaskService;

    @Test
    public void createFormTask() {
        formTaskService = new FormTaskService(new FormSubmitServiceProperties(SERVICE_CODE, SERVICE_EDITION_CODE, DATA_FORMAT_ID, DATA_FORMAT_VERSION));
        FormTask formTask = formTaskService.createFormTask(stubMessage());
        assertEquals( 1,formTask.getForms().getForm().size());
        assertEquals(SERVICE_CODE,formTask.getServiceCode());
        assertEquals(Integer.parseInt(SERVICE_EDITION_CODE),formTask.getServiceEdition());
        assertEquals(DATA_FORMAT_ID,formTask.getForms().getForm().get(0).getDataFormatId());
        assertEquals(Integer.parseInt(DATA_FORMAT_VERSION),formTask.getForms().getForm().get(0).getDataFormatVersion());
        assertEquals(ORDER_ID,formTask.getForms().getForm().get(0).getEndUserSystemReference());
        assertEquals( 0,formTask.getForms().getForm().get(0).getParentReference());
    }

    private RF1211MessageVO stubMessage() {
        return new RF1211MessageBuilder()
                .orderId(ORDER_ID)
                .orgnummer(ORGNUMMER)
                .navn(NAVN)
                .inntektsaar(INNTEKTSAAR)
                .vedlegg(VEDLEGG)
                .skattekortVarslingstype(SkattekortVarslingstype.VARSEL_VED_FOERSTE_SKATTEKORTENDRING)
                .skattekortMeldingstype(SkattekortMeldingstype.INNSENDING_MED_VEDLEGG_OG_EVT_ENDRINGER)
                .skattekortHenteEndredeSkattekort(SkattekortHenteEndredeSkattekort.SIDEN_SISTE_FORESPOERSEL)
                .sidenDato(SIDEN_DATO)
                .build();
    }

}