package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfReferenceExternalBE;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2;
import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage;
import no.altinn.intermediaryinboundexternalec.ObjectFactory;
import no.altinn.intermediaryinboundexternalec.ReceiptExternalBE;
import no.altinn.intermediaryinboundexternalec.ReceiptStatusExternal;
import no.altinn.intermediaryinboundexternalec.ReferenceExternalBE;
import no.altinn.intermediaryinboundexternalec.ReferenceTypeExternal;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoapAltinnFormSubmitConsumerServiceTest {

    private static final String SERVICE_CODE = "serviceCode";
    private static final String SERVICE_EDITION_CODE = "1";
    private static final String DATA_FORMAT_ID = "dataFormatId";
    private static final String DATA_FORMAT_VERSION = "2";

    private static final String ORDER_ID = "orderId";
    private static final String ORGNUMMER = "orgnummer";
    private static final byte[] VEDLEGG = {1, 2, 3};
    private static final String REFERENCE_VALUE = "test";
    private static final int RECEIPT_ID = 123;
    private static final String FORM_DATA = "formData";

    @Mock
    private SecurityCredentials credentials;

    @Spy
    private IIntermediaryInboundExternalEC2 iIntermediaryInboundExternalEC2;

    @Spy
    private FormTaskShipmentService formTaskShipmentService = new FormTaskShipmentService(new FormSubmitServiceProperties(SERVICE_CODE, SERVICE_EDITION_CODE, DATA_FORMAT_ID, DATA_FORMAT_VERSION));

    @InjectMocks
    private SoapAltinnFormSubmitConsumerService soapAltinnFormSubmitConsumerService;

    @Test
    void submitForm() throws IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage {
        when(iIntermediaryInboundExternalEC2.submitFormTaskEC(any(), any(), any())).thenReturn(getReceiptExternalBE());
        SubmitFormTask submitFormTask = soapAltinnFormSubmitConsumerService.submitForm(stubMessage());

        assertEquals(REFERENCE_VALUE, submitFormTask.getReceiversReference());
        assertEquals(ORDER_ID, submitFormTask.getExternalShipmentReference());
        assertEquals(RECEIPT_ID, submitFormTask.getReceiptId());
    }

    @Test
    void submitFormWithoutAttachment() throws IIntermediaryInboundExternalEC2SubmitFormTaskECAltinnFaultFaultFaultMessage {
        when(iIntermediaryInboundExternalEC2.submitFormTaskEC(any(), any(), any())).thenReturn(getReceiptExternalBE());
        SubmitFormTask submitFormTask = soapAltinnFormSubmitConsumerService.submitFormWithoutAttachment(stubMessage());
        assertEquals(REFERENCE_VALUE, submitFormTask.getReceiversReference());
        assertEquals(ORDER_ID, submitFormTask.getExternalShipmentReference());
        assertEquals(RECEIPT_ID, submitFormTask.getReceiptId());
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

    private RFMessageStub stubMessage() {
        return new RFMessageStub(ORDER_ID, ORGNUMMER, FORM_DATA, VEDLEGG);
    }
}