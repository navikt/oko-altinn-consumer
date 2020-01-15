package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfAttachment;
import no.altinn.intermediaryinboundexternalec.ArrayOfForm;
import no.altinn.intermediaryinboundexternalec.Attachment;
import no.altinn.intermediaryinboundexternalec.Form;
import no.altinn.intermediaryinboundexternalec.FormTask;
import no.altinn.intermediaryinboundexternalec.FormTaskShipmentBE;
import no.altinn.intermediaryinboundexternalec.ObjectFactory;
import no.nav.okonomi.altinn.consumer.utility.ByteUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FormTaskShipmentService {

    private final ObjectFactory objectFactory;
    private final FormSubmitServiceProperties props;

    public FormTaskShipmentService(FormSubmitServiceProperties props) {
        Objects.requireNonNull(props, "formSubmitServiceProps must not be null");
        this.props = props;
        objectFactory = new ObjectFactory();
    }

    FormTaskShipmentBE createFormTaskShipment(AltinnMessage altinnMessage) {
        FormTaskShipmentBE formTaskShipment = objectFactory.createFormTaskShipmentBE();
        formTaskShipment.setReportee(altinnMessage.getOrgnummer());
        String externalShipmentRef = props.getExternalShipmentReferencePrefix() + altinnMessage.getOrderId();
        formTaskShipment.setExternalShipmentReference(externalShipmentRef);
        formTaskShipment.setFormTasks(createFormTask(altinnMessage));
        formTaskShipment.setAttachments(createAttachment(altinnMessage.getOrderId(),
                altinnMessage.getAttachmentData(),
                ByteUtil.getMimeContentType(altinnMessage.getAttachmentData())));

        return formTaskShipment;
    }

    FormTaskShipmentBE createFormTaskShipmentWithoutAttachments(AltinnMessage message) {
        FormTaskShipmentBE formTaskShipment = objectFactory.createFormTaskShipmentBE();
        formTaskShipment.setReportee(message.getOrgnummer());
        formTaskShipment.setExternalShipmentReference(message.getOrderId());
        formTaskShipment.setFormTasks(createFormTask(message));

        return formTaskShipment;
    }


    FormTask createFormTask(AltinnMessage altinnMessage) {
        FormTask formTask = objectFactory.createFormTask();
        formTask.setServiceCode(props.getServiceCode());
        formTask.setServiceEdition(Integer.valueOf(props.getServiceEditionCode()));
        formTask.setForms(createForms(altinnMessage));

        return formTask;
    }

    ArrayOfForm createForms(AltinnMessage altinnMessage) {
        ArrayOfForm arrayOfForm = objectFactory.createArrayOfForm();
        Form form = objectFactory.createForm();
        form.setCompleted(Boolean.TRUE);
        form.setDataFormatId(props.getDataFormatId());
        form.setDataFormatVersion(Integer.valueOf(props.getDataFormatVersion()));
        form.setEndUserSystemReference(altinnMessage.getOrderId());
        form.setParentReference(0);
        form.setFormData(altinnMessage.getFormData());
        arrayOfForm.getForm().add(form);

        return arrayOfForm;
    }

    ArrayOfAttachment createAttachment(String orderId, byte[] byteArray, String mime) {
        String filename = getFilename(orderId, mime);
        ArrayOfAttachment arrayOfAttachment = objectFactory.createArrayOfAttachment();
        Attachment attachment = objectFactory.createAttachment();
        attachment.setAttachementData(byteArray);
        attachment.setName(filename);
        attachment.setFileName(filename);
        attachment.setParentReference(orderId);
        attachment.setEndUserSystemReference(orderId);
        attachment.setEncrypted(ByteUtil.APPLICATION_PKCS7_MIME.equals(mime));
        arrayOfAttachment.getAttachment().add(attachment);

        return arrayOfAttachment;
    }

    private String getFilename(String id, String mime) {
        String ext;
        switch (mime) {
            case ByteUtil.APPLICATION_PKCS7_MIME:
                ext = ".enc";
                break;
            case ByteUtil.APPLICATION_X_GZIP:
            case ByteUtil.APPLICATION_X_BZIP2:
            case ByteUtil.APPLICATION_X_COMPRESSED:
            case ByteUtil.APPLICATION_ZIP:
                ext = ".zip";
                break;
            default:
                ext = ".xml";
                break;
        }

        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS_")) + id + ext;
    }

}
