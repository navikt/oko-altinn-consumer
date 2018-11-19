package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfAttachment;
import no.altinn.intermediaryinboundexternalec.FormTask;
import no.altinn.intermediaryinboundexternalec.FormTaskShipmentBE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FormTaskShipmentServiceTest {

    private static final String ORDER_ID = "orderId";
    private static final String ORGNUMMER = "orgnummer";
    private static final String NAVN = "navn";
    private static final String INNTEKTSAAR = "2017";
    private static final byte[] VEDLEGG = {1, 2, 3};
    private static final String SIDEN_DATO = "sidenDato";

    @InjectMocks
    private FormTaskShipmentService formTaskShipmentService;

    @Mock
    private FormTaskService formTaskService;

    @Mock
    private AttachmentService attachmentService;

    @Test
    public void createFormTaskShipment() {
        FormTask formTask = new FormTask();
        ArrayOfAttachment arrayOfAttachment = new ArrayOfAttachment();

        RF1211MessageVO message = stubMessage();

        when(formTaskService.createFormTask(message)).thenReturn(formTask);
        when(attachmentService.createAttachment(anyString(), any(byte[].class), anyString())).thenReturn(arrayOfAttachment);

        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(message);

        assertThat(formTaskShipment.getReportee(), is(ORGNUMMER));
        assertThat(formTaskShipment.getExternalShipmentReference(), is(ORDER_ID));
        assertThat(formTaskShipment.getFormTasks(), is(formTask));
        assertThat(formTaskShipment.getAttachments(), is(arrayOfAttachment));
    }

    @Test
    public void createFormTaskShipmentWithoutAttachments() {
        FormTask formTask = new FormTask();

        RF1211MessageVO message = stubMessage();

        when(formTaskService.createFormTask(message)).thenReturn(formTask);

        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(message);

        assertThat(formTaskShipment.getReportee(), is(ORGNUMMER));
        assertThat(formTaskShipment.getExternalShipmentReference(), is(ORDER_ID));
        assertThat(formTaskShipment.getFormTasks(), is(formTask));
        assertNull(formTaskShipment.getAttachments());
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