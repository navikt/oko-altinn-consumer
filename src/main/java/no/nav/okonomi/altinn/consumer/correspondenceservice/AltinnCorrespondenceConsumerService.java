package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.altinn.correspondenceexternalec.*;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import no.nav.okonomi.altinn.consumer.utility.XMLUtil;
import no.nav.okonomi.altinn.consumer.utility.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Levent Demir (Capgemini)
 */
public class AltinnCorrespondenceConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AltinnCorrespondenceConsumerService.class);

    private static final String FEIL_VED_AA_MOTTA = "Feil ved å motta melding fra Altinn: ";

    private int languageId;

    private final ICorrespondenceExternalEC2 iCorrespondenceExternalEC2;

    private SecurityCredentials credentials;

    public AltinnCorrespondenceConsumerService(ICorrespondenceExternalEC2 iCorrespondenceExternalEC2,
                                               SecurityCredentials credentials, int languageId) {
        if (iCorrespondenceExternalEC2 == null || credentials == null) {
            throw new IllegalArgumentException(
                    "ICorrespondenceExternalEC2 == " + iCorrespondenceExternalEC2
                            + " SecurityCredentials == " + credentials);
        }
        this.iCorrespondenceExternalEC2 = iCorrespondenceExternalEC2;
        this.credentials = credentials;
        this.languageId = languageId;
    }

    public synchronized Document retrieveDocument(SubmitFormTask submitFormTask) {
        CorrespondenceForEndUserSystemV2 correspondence = correspondence(Integer.valueOf(submitFormTask.getReceiversReference()));
        JAXBElement<AttachmentBEV2List> attList = correspondence.getCorrespondenceAttachments();

        if (attList != null) {
            Optional<AttachmentBEV2> attachmentBE = attList.getValue().getAttachmentBEV2().stream()
                    .filter(att -> att.getAttachmentTypeID().equals(AttachmentType.TEXT_XML) || att.getAttachmentTypeID().equals(AttachmentType.APPLICATION_ZIP))
                    .findFirst();

            if (attachmentBE.isPresent()) {
                byte[] bytes = attachmentBE.get().getAttachmentData().getValue();
                LOGGER.info("Mottatt vedlagg fra Altinn med reportee element id: {} \n\rLogtekst: {}", submitFormTask.getReceiversReference(), getLogText(correspondence, bytes.length));
                try {
                    bytes = decompress(bytes, Integer.valueOf(submitFormTask.getReceiversReference()));
                    return XMLUtil.getDocument(bytes, true);
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    throw new AltinnCorrespondenceServiceException("Feil ved å behandle vedlegget fra Altinn: ", e);
                }
            }
        }
        LOGGER.warn("Ingen vedlegg mottat type av {} eller {}", AttachmentType.TEXT_XML, AttachmentType.APPLICATION_ZIP);
        return null;
    }

    public synchronized void test() {
        try {
            iCorrespondenceExternalEC2.test();
        } catch (Exception e) {
            LOGGER.warn("Feil ved å motta test melding fra Altinn: {}", e.getMessage());
            throw new AltinnCorrespondenceServiceException("Feil ved å motta test melding fra Altinn: ", e);
        }
    }

    private CorrespondenceForEndUserSystemV2 correspondence(Integer reporteeElementID) {
        try {
            return iCorrespondenceExternalEC2.getCorrespondenceForEndUserSystemsEC(credentials.getVirksomhetsbruker(), credentials.getVirksomhetsbrukerPassord(), reporteeElementID, languageId);
        } catch (ICorrespondenceExternalEC2GetCorrespondenceForEndUserSystemsECAltinnFaultFaultFaultMessage altinne) {
            LOGGER.error(getAltinnErrorMessage(altinne));
            throw new AltinnCorrespondenceServiceException(FEIL_VED_AA_MOTTA, altinne);
        } catch (IllegalArgumentException iae) {
            LOGGER.error("Melding feilet hos mottakeren: {}", iae);
            throw new AltinnCorrespondenceServiceException(FEIL_VED_AA_MOTTA, iae);
        } catch (Exception e) {
            LOGGER.error("Ukjent feil oppstått: {}", e);
            throw new AltinnCorrespondenceServiceException(FEIL_VED_AA_MOTTA, e);
        }

    }

    private String getAltinnErrorMessage(ICorrespondenceExternalEC2GetCorrespondenceForEndUserSystemsECAltinnFaultFaultFaultMessage altinne) {
        AltinnFault fault = altinne.getFaultInfo();
        String errMsg = fault.getAltinnErrorMessage().getValue();
        Integer errId = fault.getErrorID();
        return "ErrorMessage:" + errMsg + '/' +
                "ExtendedErrorMessage:" + fault.getAltinnExtendedErrorMessage().getValue() + '/' +
                "LocalizedErrorMessage:" + fault.getAltinnLocalizedErrorMessage().getValue() + '/' +
                "ErrorGuid:" + fault.getErrorGuid().getValue() + '/' +
                "ErrorID:" + errId + '/' +
                "UserGuid:" + fault.getUserGuid().getValue() + '/' +
                "UserId:" + fault.getUserId().getValue();
    }

    private String getLogText(CorrespondenceForEndUserSystemV2 correspondenceForEndUserSystem, int size) {
        CorrespondenceV2 correspondence = correspondenceForEndUserSystem.getCorrespondence().getValue();
        return "[title: " + correspondence.getCorrespondenceTitle() + ']' +
                "[CorrespondenceID: " + correspondence.getCorrespondenceID() + ']' +
                "[size: " + size + ']';
    }

    private byte[] decompress(byte[] bytes, Integer reporteeElementID) throws IOException {
        int before = bytes.length;
        byte[] decompressedBytes;
        if (ZipUtil.isPkzipped(bytes)) {
            decompressedBytes = ZipUtil.unZipByteArray(bytes);
        } else if (ZipUtil.isGzipped(bytes)) {
            decompressedBytes = ZipUtil.unGzipByteArray(bytes);
        } else {
            decompressedBytes = bytes;
        }
        LOGGER.info("Meldingen med reportee element id: {}  er dekomprimert, lengde før: {} byte, lengde etter: {} byte", reporteeElementID, before, decompressedBytes != null ? decompressedBytes.length : 0);
        return decompressedBytes;
    }
}