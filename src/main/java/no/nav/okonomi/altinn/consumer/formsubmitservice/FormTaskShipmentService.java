package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.FormTaskShipmentBE;
import no.altinn.intermediaryinboundexternalec.ObjectFactory;
import no.nav.okonomi.altinn.consumer.utility.ByteUtil;

public class FormTaskShipmentService {

    private ObjectFactory objectFactory;
    private AttachmentService attachmentService;
    private FormTaskService formTaskService;

    public FormTaskShipmentService(AttachmentService attachmentService, FormTaskService formTaskService) {
        this.attachmentService = attachmentService;
        this.formTaskService = formTaskService;
        objectFactory = new ObjectFactory();
    }

    FormTaskShipmentBE createFormTaskShipment(AltinnMessage altinnMessage) {
        FormTaskShipmentBE formTaskShipment = objectFactory.createFormTaskShipmentBE();
        formTaskShipment.setReportee(altinnMessage.getOrgnummer());
        formTaskShipment.setExternalShipmentReference(altinnMessage.getOrderId());
        formTaskShipment.setFormTasks(formTaskService.createFormTask(altinnMessage));
        formTaskShipment.setAttachments(attachmentService.createAttachment(altinnMessage.getOrderId(),
                altinnMessage.getAttachmentData(),
                ByteUtil.getMimeContentType(altinnMessage.getAttachmentData())));

        return formTaskShipment;
    }

    FormTaskShipmentBE createFormTaskShipmentWithoutAttachments(AltinnMessage message) {
        FormTaskShipmentBE formTaskShipment = objectFactory.createFormTaskShipmentBE();
        formTaskShipment.setReportee(message.getOrgnummer());
        formTaskShipment.setExternalShipmentReference(message.getOrderId());
        formTaskShipment.setFormTasks(formTaskService.createFormTask(message));

        return formTaskShipment;
    }
}
