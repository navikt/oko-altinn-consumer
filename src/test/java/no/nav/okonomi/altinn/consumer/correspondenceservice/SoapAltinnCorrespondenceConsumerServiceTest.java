package no.nav.okonomi.altinn.consumer.correspondenceservice;

import no.altinn.correspondenceexternalec.AttachmentBEV2;
import no.altinn.correspondenceexternalec.AttachmentBEV2List;
import no.altinn.correspondenceexternalec.AttachmentType;
import no.altinn.correspondenceexternalec.CorrespondenceForEndUserSystemV2;
import no.altinn.correspondenceexternalec.CorrespondenceV2;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2;
import no.altinn.correspondenceexternalec.ICorrespondenceExternalEC2GetCorrespondenceForEndUserSystemsECAltinnFaultFaultFaultMessage;
import no.altinn.correspondenceexternalec.ObjectFactory;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBElement;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoapAltinnCorrespondenceConsumerServiceTest {

    private static final int LANGUAGE_ID = 1044;

    @Mock
    private ICorrespondenceExternalEC2 correspondenceService;

    @Mock
    private SecurityCredentials securityCredentials;

    private SoapAltinnCorrespondenceConsumerService altinnCorrespondenceConsumerService;

    @BeforeEach
    void setUp() {
        altinnCorrespondenceConsumerService = new SoapAltinnCorrespondenceConsumerService(correspondenceService, securityCredentials, LANGUAGE_ID);
    }

    @Test
    void shouldReturnDocument() throws ICorrespondenceExternalEC2GetCorrespondenceForEndUserSystemsECAltinnFaultFaultFaultMessage, IOException {
        CorrespondenceForEndUserSystemV2 correspondenceForEndUserSystemV2 = createCorrespondenceForEndUserSystemV2();
        when(correspondenceService.getCorrespondenceForEndUserSystemsEC(null, null, 4973347, LANGUAGE_ID)).thenReturn(correspondenceForEndUserSystemV2);

        SubmitFormTask sft = new SubmitFormTask(11663407, "90f70a46-cb4e-4dcc-9138-45bc3fdf8f91", "AR3697057", "4973347");
        Document document = altinnCorrespondenceConsumerService.retrieveDocument(sft);
        assertNotNull(document);
    }

    private CorrespondenceForEndUserSystemV2 createCorrespondenceForEndUserSystemV2() throws IOException {
        ObjectFactory factory = new ObjectFactory();
        CorrespondenceForEndUserSystemV2 correspondenceForEndUserSystemV2 = factory.createCorrespondenceForEndUserSystemV2();
        JAXBElement<CorrespondenceV2> correspondenceForEndUserSystemV2Correspondence = factory.createCorrespondenceForEndUserSystemV2Correspondence(getCorrespondenceV2(factory));
        correspondenceForEndUserSystemV2.setCorrespondence(correspondenceForEndUserSystemV2Correspondence);
        AttachmentBEV2List attachmentBEV2List = factory.createAttachmentBEV2List();
        attachmentBEV2List.getAttachmentBEV2().add(getAttachmentBEV2(factory));
        JAXBElement<AttachmentBEV2List> correspondenceForEndUserSystemV2CorrespondenceAttachments = factory.createCorrespondenceForEndUserSystemV2CorrespondenceAttachments(attachmentBEV2List);
        correspondenceForEndUserSystemV2.setCorrespondenceAttachments(correspondenceForEndUserSystemV2CorrespondenceAttachments);
        return correspondenceForEndUserSystemV2;
    }

    private AttachmentBEV2 getAttachmentBEV2(ObjectFactory factory) throws IOException {
        AttachmentBEV2 attachmentBEV2 = factory.createAttachmentBEV2();
        attachmentBEV2.setAttachmentTypeID(AttachmentType.TEXT_XML);
        InputStream resourceAsStream = getClass().getResourceAsStream("/attachments/skattekortTilArbeidsgiver_v2.xml");
        byte[] data = IOUtils.toByteArray(resourceAsStream);

        JAXBElement<byte[]> attachmentBEV2AttachmentData = factory.createAttachmentBEV2AttachmentData(data);
        attachmentBEV2.setAttachmentData(attachmentBEV2AttachmentData);
        return attachmentBEV2;
    }

    private CorrespondenceV2 getCorrespondenceV2(ObjectFactory factory) {
        CorrespondenceV2 correspondenceV2 = factory.createCorrespondenceV2();
        JAXBElement<String> correspondenceV2TitleTest = factory.createCorrespondenceV2CorrespondenceTitle("CorrespondenceV2TitleTest");
        correspondenceV2.setCorrespondenceTitle(correspondenceV2TitleTest);
        correspondenceV2.setCorrespondenceID(123);
        return correspondenceV2;
    }
}
