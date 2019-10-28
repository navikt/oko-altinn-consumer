package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfAttachment;
import no.altinn.intermediaryinboundexternalec.FormTask;
import no.altinn.intermediaryinboundexternalec.FormTaskShipmentBE;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FormTaskShipmentServiceTest {

    private static final String ORDER_ID = "orderId";
    private static final String ORGNUMMER = "orgnummer";
    private static final byte[] VEDLEGG = {1, 2, 3};

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

        RFMessageStub message = stubMessage();

        when(formTaskService.createFormTask(message)).thenReturn(formTask);
        when(attachmentService.createAttachment(anyString(), any(byte[].class), anyString())).thenReturn(arrayOfAttachment);

        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(message);

        assertEquals(ORGNUMMER, formTaskShipment.getReportee());
        assertEquals(ORDER_ID, formTaskShipment.getExternalShipmentReference());
        assertEquals(formTask, formTaskShipment.getFormTasks());
        assertEquals(arrayOfAttachment, formTaskShipment.getAttachments());
    }

    @Test
    public void createFormTaskShipmentWithoutAttachments() {
        FormTask formTask = new FormTask();

        RFMessageStub message = stubMessage();

        when(formTaskService.createFormTask(message)).thenReturn(formTask);

        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(message);

        assertEquals(ORGNUMMER, formTaskShipment.getReportee());
        assertEquals(ORDER_ID, formTaskShipment.getExternalShipmentReference());
        assertEquals(formTask, formTaskShipment.getFormTasks());
        assertNull(formTaskShipment.getAttachments());
    }

    //todo formdata test
    private RFMessageStub stubMessage() {
        return new RFMessageStub(ORDER_ID, ORGNUMMER, null, VEDLEGG);
    }

}