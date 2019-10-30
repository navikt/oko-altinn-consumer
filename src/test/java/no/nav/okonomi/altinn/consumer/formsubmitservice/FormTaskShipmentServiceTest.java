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
    private static final String SERVICE_CODE = "serviceCode";
    private static final String SERVICE_EDITION_CODE = "1";
    private static final String DATA_FORMAT_ID = "dataFormatId";
    private static final String DATA_FORMAT_VERSION = "2";
    private static final String FORM_DATA = "formData";


    private FormTaskShipmentService formTaskShipmentService = new FormTaskShipmentService(new FormSubmitServiceProperties(SERVICE_CODE,SERVICE_EDITION_CODE,DATA_FORMAT_ID,DATA_FORMAT_VERSION));

    @Test
    public void createFormTaskShipment() {
        RFMessageStub message = stubMessage();
        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipment(message);
        assertEquals(ORGNUMMER, formTaskShipment.getReportee());
        assertEquals(ORDER_ID, formTaskShipment.getExternalShipmentReference());
        assertNotNull(formTaskShipment.getFormTasks());
        assertNotNull(formTaskShipment.getAttachments());
    }

    @Test
    public void createFormTaskShipmentWithoutAttachments() {
        RFMessageStub message = stubMessage();
        FormTaskShipmentBE formTaskShipment = formTaskShipmentService.createFormTaskShipmentWithoutAttachments(message);
        assertEquals(ORGNUMMER, formTaskShipment.getReportee());
        assertEquals(ORDER_ID, formTaskShipment.getExternalShipmentReference());
        assertNotNull(formTaskShipment.getFormTasks());
        assertNull(formTaskShipment.getAttachments());
    }

    @Test
    public void createFormTask() {
        FormTask formTask = formTaskShipmentService.createFormTask(stubMessage());
        assertEquals(1, formTask.getForms().getForm().size());
        assertEquals(SERVICE_CODE, formTask.getServiceCode());
        assertEquals(Integer.parseInt(SERVICE_EDITION_CODE), formTask.getServiceEdition());
        assertEquals(DATA_FORMAT_ID, formTask.getForms().getForm().get(0).getDataFormatId());
        assertEquals(Integer.parseInt(DATA_FORMAT_VERSION), formTask.getForms().getForm().get(0).getDataFormatVersion());

        assertNotNull(formTask.getForms().getForm().get(0).getFormData());

        assertEquals(ORDER_ID, formTask.getForms().getForm().get(0).getEndUserSystemReference());
        assertEquals(0, formTask.getForms().getForm().get(0).getParentReference());
    }

    @Test
    public void createAttachment() {
        String orderId = "orderId";
        byte[] byteArray = {1, 2, 3};
        String mime = "xml";

        ArrayOfAttachment arrayOfAttachment = formTaskShipmentService.createAttachment(orderId, byteArray, mime);

        assertEquals(1, arrayOfAttachment.getAttachment().size());
        assertEquals(byteArray, arrayOfAttachment.getAttachment().get(0).getAttachementData());
        assertEquals(orderId, arrayOfAttachment.getAttachment().get(0).getParentReference());
        assertEquals(orderId, arrayOfAttachment.getAttachment().get(0).getEndUserSystemReference());
        assertEquals(false, arrayOfAttachment.getAttachment().get(0).isEncrypted());
    }

    private RFMessageStub stubMessage() {
        return new RFMessageStub(ORDER_ID, ORGNUMMER, FORM_DATA, VEDLEGG);
    }

}