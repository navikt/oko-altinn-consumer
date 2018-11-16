package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.FormTask;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnFormSubmitConsumerConfig.PropertyMapKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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

    @InjectMocks
    private FormTaskService formTaskService;

    @Mock
    private Map<PropertyMapKey, String> map;

    @Test
    public void createFormTask() throws Exception {
        when(map.get(PropertyMapKey.SERVICE_CODE)).thenReturn(SERVICE_CODE);
        when(map.get(PropertyMapKey.SERVICE_EDITION_CODE)).thenReturn(SERVICE_EDITION_CODE);
        when(map.get(PropertyMapKey.DATA_FORMAT_ID)).thenReturn(DATA_FORMAT_ID);
        when(map.get(PropertyMapKey.DATA_FORMAT_VERSION)).thenReturn(DATA_FORMAT_VERSION);
        when(map.get(PropertyMapKey.LANGUAGE_ID)).thenReturn(LANGUAGE_ID);

        FormTask formTask = formTaskService.createFormTask(stubMessage());

        assertThat(formTask.getForms().getForm().size(), is(1));
        assertThat(formTask.getServiceCode(), is(SERVICE_CODE));
        assertThat(formTask.getServiceEdition(), is(Integer.parseInt(SERVICE_EDITION_CODE)));
        assertThat(formTask.getForms().getForm().get(0).getDataFormatId(), is(DATA_FORMAT_ID));
        assertThat(formTask.getForms().getForm().get(0).getDataFormatVersion(), is(Integer.parseInt(DATA_FORMAT_VERSION)));
        assertThat(formTask.getForms().getForm().get(0).getEndUserSystemReference(), is(ORDER_ID));
        assertThat(formTask.getForms().getForm().get(0).getParentReference(), is(0));
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