package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.*;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultAltinnFormSubmitConsumerServiceTest {

    private static final String SERVICE_CODE = "serviceCode";
    private static final String SERVICE_EDITION_CODE = "1";
    private static final String DATA_FORMAT_ID = "dataFormatId";
    private static final String DATA_FORMAT_VERSION = "2";

    private static final String ORDER_ID = "orderId";
    private static final String ORGNUMMER = "orgnummer";
    private static final String NAVN = "navn";
    private static final String INNTEKTSAAR = "2017";
    private static final byte[] VEDLEGG = {1, 2, 3};
    private static final String SIDEN_DATO = "sidenDato";
    public static final String REFERENCE_VALUE = "test";
    public static final int RECEIPT_ID = 123;

    @Mock
    private SecurityCredentials credentials;

    @Spy
    private IIntermediaryInboundExternalEC2 iIntermediaryInboundExternalEC2;

    @Spy
    private FormTaskShipmentService formTaskShipmentService = new FormTaskShipmentService(new AttachmentService(), new FormTaskService(new FormSubmitServiceProperties(SERVICE_CODE, SERVICE_EDITION_CODE, DATA_FORMAT_ID, DATA_FORMAT_VERSION)));

    @InjectMocks
    private DefaultAltinnFormSubmitConsumerService altinnFormSubmitConsumerService;

    @Test
    void submitForm() throws IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage {
        when(iIntermediaryInboundExternalEC2.submitFormTaskEC(any(),any(),any())).thenReturn(getReceiptExternalBE());
        SubmitFormTask submitFormTask = altinnFormSubmitConsumerService.submitForm(stubMessage());

        assertEquals(REFERENCE_VALUE,submitFormTask.getReceiversReference());
        assertEquals(ORDER_ID, submitFormTask.getExternalShipmentReference());
        assertEquals(RECEIPT_ID,submitFormTask.getReceiptId());
    }

    @Test
    void submitFormWithoutAttachment() throws IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage {
        when(iIntermediaryInboundExternalEC2.submitFormTaskEC(any(),any(),any())).thenReturn(getReceiptExternalBE());
        SubmitFormTask submitFormTask = altinnFormSubmitConsumerService.submitFormWithoutAttachment(stubMessage());
        assertEquals(REFERENCE_VALUE,submitFormTask.getReceiversReference());
        assertEquals(ORDER_ID, submitFormTask.getExternalShipmentReference());
        assertEquals(RECEIPT_ID,submitFormTask.getReceiptId());
    }

    private ReceiptExternalBE getReceiptExternalBE() {
        ReceiptExternalBE receiptExternalBE = new ReceiptExternalBE();
        receiptExternalBE.setReceiptStatusCode(ReceiptStatusExternal.OK);
        ReferenceExternalBE referenceExternalBE = new ReferenceExternalBE();
        referenceExternalBE.setReferenceTypeName(ReferenceTypeExternal.RECEIVERS_REFERENCE);
        referenceExternalBE.setReferenceValue(REFERENCE_VALUE);
        receiptExternalBE.setReceiptId(RECEIPT_ID);
        ObjectFactory factory = new ObjectFactory();
        ArrayOfReferenceExternalBE arrayOfReferenceExternalBE = factory.createArrayOfReferenceExternalBE();
        receiptExternalBE.setReferences(arrayOfReferenceExternalBE);
        receiptExternalBE.getReferences().getReferenceBE().add(referenceExternalBE);
        return receiptExternalBE;
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