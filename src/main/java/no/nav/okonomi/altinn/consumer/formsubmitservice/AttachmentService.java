package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfAttachment;
import no.altinn.intermediaryinboundexternalec.Attachment;
import no.altinn.intermediaryinboundexternalec.ObjectFactory;
import no.nav.okonomi.altinn.consumer.utility.ByteUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AttachmentService {

    private ObjectFactory objectFactory = new ObjectFactory();

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
