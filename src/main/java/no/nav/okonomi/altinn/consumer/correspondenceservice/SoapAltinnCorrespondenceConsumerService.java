package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.altinn.correspondenceexternalec.AltinnFault;
import no.altinn.correspondenceexternalec.AttachmentBEV2;
import no.altinn.correspondenceexternalec.AttachmentBEV2List;
import no.altinn.correspondenceexternalec.AttachmentType;
import no.altinn.correspondenceexternalec.CorrespondenceForEndUserSystemV2;
import no.altinn.correspondenceexternalec.CorrespondenceV2;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2GetCorrespondenceForEndUserSystemsECAltinnFaultFaultFaultMessage;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2TestAltinnFaultFaultFaultMessage;
import no.nav.okonomi.altinn.consumer.AltinnConsumerInternalException;
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
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Levent Demir (Capgemini)
 */
public class SoapAltinnCorrespondenceConsumerService implements AltinnCorrespondenceConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapAltinnCorrespondenceConsumerService.class);

    private static final String FEIL_VED_AA_MOTTA = "Feil ved å motta melding fra Altinn: ";

    private final int languageId;

    private final ICorrespondenceExternalEC2 iCorrespondenceExternalEC2;

    private final SecurityCredentials credentials;

    public SoapAltinnCorrespondenceConsumerService(ICorrespondenceExternalEC2 iCorrespondenceExternalEC2,
                                                   SecurityCredentials securityCredentials, int languageId) {
        Objects.requireNonNull(iCorrespondenceExternalEC2, "iCorrespondenceExternalEC2 must not be null");
        Objects.requireNonNull(securityCredentials, "securityCredentials must not be null");
        this.iCorrespondenceExternalEC2 = iCorrespondenceExternalEC2;
        this.credentials = securityCredentials;
        this.languageId = languageId;
    }

    public synchronized Document retrieveDocument(SubmitFormTask submitFormTask) throws AltinnCorrespondenceConsumerServiceException, AltinnConsumerInternalException {
        CorrespondenceForEndUserSystemV2 correspondence = correspondence(Integer.valueOf(submitFormTask.getReceiversReference()));
        JAXBElement<AttachmentBEV2List> attList = correspondence.getCorrespondenceAttachments();

        if (attList != null) {
            Optional<AttachmentBEV2> attachmentBE = attList.getValue().getAttachmentBEV2().stream()
                    .filter(att -> att.getAttachmentTypeID().equals(AttachmentType.TEXT_XML) ||
                            att.getAttachmentTypeID().equals(AttachmentType.APPLICATION_ZIP))
                    .findFirst();

            if (attachmentBE.isPresent()) {
                byte[] bytes = attachmentBE.get().getAttachmentData().getValue();
                LOGGER.info("Mottatt vedlagg fra Altinn med reportee element id: {} \n\rLogtekst: {}",
                        submitFormTask.getReceiversReference(), getLogText(correspondence, bytes.length));
                try {
                    bytes = decompress(bytes, Integer.valueOf(submitFormTask.getReceiversReference()));
                    return XMLUtil.getDocument(bytes, true);
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    throw new AltinnConsumerInternalException("Feil ved å behandle vedlegget fra Altinn", e);
                }
            }
        }
        LOGGER.warn("Ingen vedlegg mottat type av {} eller {}", AttachmentType.TEXT_XML, AttachmentType.APPLICATION_ZIP);
        return null;//todo return null er dårlig practice
    }

    public synchronized void test() throws AltinnCorrespondenceConsumerServiceException {
        try {
            iCorrespondenceExternalEC2.test();
        } catch (ICorrespondenceExternalEC2TestAltinnFaultFaultFaultMessage altinne) {
            AltinnFault faultInfo = altinne.getFaultInfo();
            LOGGER.error(FEIL_VED_AA_MOTTA + getAltinnErrorMessage(faultInfo));
            throw new AltinnCorrespondenceConsumerServiceException(
                    FEIL_VED_AA_MOTTA,
                    getSafeString(faultInfo.getAltinnErrorMessage()),
                    faultInfo.getErrorID(),
                    altinne);
        }
    }

    private CorrespondenceForEndUserSystemV2 correspondence(Integer reporteeElementID) throws AltinnCorrespondenceConsumerServiceException {
        try {
            return iCorrespondenceExternalEC2.getCorrespondenceForEndUserSystemsEC(
                    credentials.getVirksomhetsbruker(),
                    credentials.getVirksomhetsbrukerPassord(),
                    reporteeElementID,
                    languageId);
        } catch (ICorrespondenceExternalEC2GetCorrespondenceForEndUserSystemsECAltinnFaultFaultFaultMessage altinne) {
            AltinnFault faultInfo = altinne.getFaultInfo();
            LOGGER.error(getAltinnErrorMessage(faultInfo));
            throw new AltinnCorrespondenceConsumerServiceException(
                    FEIL_VED_AA_MOTTA,
                    getSafeString(faultInfo.getAltinnErrorMessage()),
                    faultInfo.getErrorID(),
                    altinne);
        }
    }

    private String getAltinnErrorMessage(AltinnFault fault) {
        return fault == null ? "Ingen FaultInfo" : getAltinnFaultAsString(fault);
    }

    private String getAltinnFaultAsString(AltinnFault fault) {
        return "ErrorMessage:" + getSafeString(fault.getAltinnErrorMessage()) + '/' +
                "ExtendedErrorMessage:" + getSafeString(fault.getAltinnExtendedErrorMessage()) + '/' +
                "LocalizedErrorMessage:" + getSafeString(fault.getAltinnLocalizedErrorMessage()) + '/' +
                "ErrorGuid:" + getSafeString(fault.getErrorGuid()) + '/' +
                "ErrorID:" + fault.getErrorID() + '/' +
                "UserGuid:" + getSafeString(fault.getUserGuid()) + '/' +
                "UserId:" + getSafeString(fault.getUserId());
    }

    private String getSafeString(JAXBElement<String> element) {
        return element != null ? element.getValue() : "null";
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
        LOGGER.debug("Meldingen med reportee element id: {}  er dekomprimert, lengde før: {} byte, lengde etter: {} byte",
                reporteeElementID, before, decompressedBytes != null ? decompressedBytes.length : 0);
        return decompressedBytes;
    }
}