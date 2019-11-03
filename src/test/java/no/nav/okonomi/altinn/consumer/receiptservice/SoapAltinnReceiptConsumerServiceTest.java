package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.AltinnFault;
import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage;
import no.altinn.receiptexternalec.IReceiptExternalEC2TestAltinnFaultFaultFaultMessage;
import no.altinn.receiptexternalec.ReceiptStatusEnum;
import no.altinn.receiptexternalec.ReferenceType;
import no.altinn.receiptexternalec.v201506.ObjectFactory;
import no.altinn.receiptexternalec.v201506.Receipt;
import no.altinn.receiptexternalec.v201506.Reference;
import no.altinn.receiptexternalec.v201506.ReferenceList;
import no.nav.okonomi.altinn.consumer.AltinnConsumerServiceException;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.SubmitFormTaskBuilder;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.JAXBElement;

import static no.nav.okonomi.altinn.consumer.AltinnConsumerServiceException.INTERNAL_FAULT_REASON;
import static no.nav.okonomi.altinn.consumer.AltinnConsumerServiceException.INTERNAL_OR_NO_FAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoapAltinnReceiptConsumerServiceTest {

    private static final int ERROR_VALUE = 1;
    private static final String ERROR_MESSAGE = "error";
    private static final String EXCEPTION_MESSAGE = "message";

    @Mock
    private IReceiptExternalEC2 iReceiptExternalEC;

    @Mock
    private SecurityCredentials credentials;

    @Spy
    private ReceiptService receiptService;

    @InjectMocks
    SoapAltinnReceiptConsumerService altinnReceiptConsumerService;

    @Test
    void getReceiptWithSubmitForm() throws IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage, AltinnReceiptServiceException {
        Receipt receipt = getReceipt();
        when(iReceiptExternalEC.getReceiptECV2(any(), any(), any())).thenReturn(receipt);
        SubmitFormTask sft = createSubmitFormTask();
        sft = altinnReceiptConsumerService.getReceiptWithSubmitForm(sft);
        assertNotNull(sft);
    }

    @Test
    void ShouldThrowErrorMessageWithAltinnFaultWhenCallingTest() throws IReceiptExternalEC2TestAltinnFaultFaultFaultMessage {
        AltinnFault altinnFault = createAltinnFault();
        IReceiptExternalEC2TestAltinnFaultFaultFaultMessage altinnFaultFaultFaultMessage =
                new IReceiptExternalEC2TestAltinnFaultFaultFaultMessage(EXCEPTION_MESSAGE, altinnFault);
        doThrow(altinnFaultFaultFaultMessage).when(iReceiptExternalEC).test();
        AltinnReceiptServiceException exception = assertThrows(AltinnReceiptServiceException.class,
                () -> altinnReceiptConsumerService.test());
        assertEquals(AltinnConsumerServiceException.FaultCode.ALTINN_FAULT, exception.getFaultCode());
        assertEquals(ERROR_VALUE, exception.getFaultCodeValue());
        assertEquals(ERROR_MESSAGE, exception.getFaultReason());
    }

    @Test
    void ShouldThrowErrorMessageWithAltinnFaultWhenCallingSubmitForm() throws IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage {
        AltinnFault altinnFault = createAltinnFault();
        when(iReceiptExternalEC.getReceiptECV2(any(), any(), any())).
                thenThrow(new IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage(EXCEPTION_MESSAGE, altinnFault));
        SubmitFormTask sft = createSubmitFormTask();
        AltinnReceiptServiceException exception = assertThrows(AltinnReceiptServiceException.class,
                () -> altinnReceiptConsumerService.getReceiptWithSubmitForm(sft));
        assertEquals(AltinnConsumerServiceException.FaultCode.ALTINN_FAULT, exception.getFaultCode());
        assertEquals(ERROR_VALUE, exception.getFaultCodeValue());
        assertEquals(ERROR_MESSAGE, exception.getFaultReason());

    }

    @Test
    void ShouldThrowErrorMessageWithoutAltinnFaultWhenCallingSubmitForm() throws IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage {
        Receipt receipt = getReceipt();
        receipt.setReceiptStatus(ReceiptStatusEnum.UN_EXPECTED_ERROR);//TODO hva med REJECTED, VALDIDATION_FAILD mm
        when(iReceiptExternalEC.getReceiptECV2(any(), any(), any())).thenReturn(receipt);
        SubmitFormTask sft = createSubmitFormTask();
        AltinnReceiptServiceException exception = assertThrows(AltinnReceiptServiceException.class,
                () -> altinnReceiptConsumerService.getReceiptWithSubmitForm(sft));
        assertEquals(AltinnConsumerServiceException.FaultCode.INTERNAL_FAULT, exception.getFaultCode());
        assertEquals(INTERNAL_OR_NO_FAULT, exception.getFaultCodeValue());
        assertEquals(INTERNAL_FAULT_REASON, exception.getFaultReason());

    }

    private SubmitFormTask createSubmitFormTask() {
        return new SubmitFormTaskBuilder()
                .receipdId(11663407)
                .externalShipmentReference("90f70a46-cb4e-4dcc-9138-45bc3fdf8f91")
                .build();
    }

    private Receipt getReceipt() {
        ObjectFactory factory = new ObjectFactory();
        Receipt receipt = getReceipt(factory);
        ReferenceList referenceList = factory.createReferenceList();
        referenceList.getReference().add(getReference(factory));
        receipt.setReferences(factory.createReceiptReferences(referenceList));
        return receipt;
    }

    private Reference getReference(ObjectFactory factory) {
        Reference reference = factory.createReference();
        reference.setReferenceType(ReferenceType.RECEIVERS_REFERENCE);
        reference.setReferenceValue("90f70a46-cb4e-4dcc-9138-45bc3fdf8f91");
        return reference;
    }

    private Receipt getReceipt(ObjectFactory factory) {
        Receipt receipt = factory.createReceipt();
        receipt.setReceiptId(11663407);
        receipt.setReceiptStatus(ReceiptStatusEnum.OK);
        return receipt;
    }

    private AltinnFault createAltinnFault() {
        no.altinn.receiptexternalec.ObjectFactory factory = new no.altinn.receiptexternalec.ObjectFactory();
        JAXBElement<String> error = factory.createAltinnFaultAltinnErrorMessage(ERROR_MESSAGE);
        AltinnFault altinnFault = new AltinnFault();
        altinnFault.setAltinnErrorMessage(error);
        altinnFault.setErrorID(ERROR_VALUE);
        return altinnFault;
    }
}