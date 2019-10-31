package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.IReceiptExternalEC2;
import no.altinn.receiptexternalec.IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage;
import no.altinn.receiptexternalec.ReceiptStatusEnum;
import no.altinn.receiptexternalec.ReferenceType;
import no.altinn.receiptexternalec.v201506.ObjectFactory;
import no.altinn.receiptexternalec.v201506.Receipt;
import no.altinn.receiptexternalec.v201506.Reference;
import no.altinn.receiptexternalec.v201506.ReferenceList;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.SubmitFormTaskBuilder;
import no.nav.okonomi.altinn.consumer.security.SecurityCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SoapAltinnReceiptConsumerServiceTest {

    @Mock
    private IReceiptExternalEC2 iReceiptExternalEC;

    @Mock
    private SecurityCredentials credentials;

    @Spy
    private ReceiptService receiptService;

    @InjectMocks
    SoapAltinnReceiptConsumerService altinnReceiptConsumerService;

    @Test
    void getReceiptWithSubmitForm() throws IReceiptExternalEC2GetReceiptECV2AltinnFaultFaultFaultMessage {
        Receipt receipt = getReceipt();
        when(iReceiptExternalEC.getReceiptECV2(any(), any(), any())).thenReturn(receipt);
        SubmitFormTask sft = new SubmitFormTaskBuilder()
                .receipdId(11663407)
                .externalShipmentReference("90f70a46-cb4e-4dcc-9138-45bc3fdf8f91")
                .build();
        sft = altinnReceiptConsumerService.getReceiptWithSubmitForm(sft);
        assertNotNull(sft);
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

    //TODO more tests
}