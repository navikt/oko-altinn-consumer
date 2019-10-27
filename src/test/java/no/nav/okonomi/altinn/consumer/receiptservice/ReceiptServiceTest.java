package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.ReceiptStatusEnum;
import no.altinn.receiptexternalec.ReferenceType;
import no.altinn.receiptexternalec.v201506.*;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import no.nav.okonomi.altinn.consumer.SubmitFormTaskBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptServiceTest {

    private ReceiptService receiptService = new ReceiptService();

    private static final String ARCHIVE_REF = "archiveRef";
    private static final String RECIEVER_REF = "recieverRef";
    private static final Integer RECEIPT_ID = 1;
    private static final String EXT_SHIPMENT_REF = "extShipmentRef";

    @Test
    public void createReceipt() {
        Integer receiptId = 1;
        String extShipmentReference = "extShipmentReference";

        ReceiptSearch receiptSearch = receiptService.createReceipt(receiptId, extShipmentReference);

        assertEquals(receiptId, receiptSearch.getReceiptId());
        assertEquals(1, receiptSearch.getReferences().getValue().getReference().size());
        assertEquals(ReferenceType.EXTERNAL_SHIPMENT_REFERENCE, receiptSearch.getReferences().getValue().getReference().get(0).getReferenceType());
        assertEquals(extShipmentReference, receiptSearch.getReferences().getValue().getReference().get(0).getReferenceValue());
    }

    @Test
    public void updateReceipt_recepitstatus_OK() {
        ObjectFactory objectFactory = new ObjectFactory();
        Receipt receipt = objectFactory.createReceipt();
        receipt.setReceiptStatus(ReceiptStatusEnum.OK);

        Reference referenceArchiveRef = objectFactory.createReference();
        referenceArchiveRef.setReferenceType(ReferenceType.ARCHIVE_REFERENCE);
        referenceArchiveRef.setReferenceValue(ARCHIVE_REF);

        Reference referenceRecieverRef = objectFactory.createReference();
        referenceRecieverRef.setReferenceType(ReferenceType.RECEIVERS_REFERENCE);
        referenceRecieverRef.setReferenceValue(RECIEVER_REF);

        ReferenceList referenceList = new ReferenceList();
        referenceList.getReference().add(referenceArchiveRef);
        referenceList.getReference().add(referenceRecieverRef);

        receipt.setReferences(objectFactory.createReferenceList(referenceList));

        SubmitFormTask submitFormTask = new SubmitFormTaskBuilder()
                .receipdId(RECEIPT_ID)
                .externalShipmentReference(EXT_SHIPMENT_REF)
                .build();

        SubmitFormTask returnedValue = receiptService.updateReceipt(receipt, submitFormTask);

        assertEquals(RECEIPT_ID, returnedValue.getReceiptId());
        assertEquals(EXT_SHIPMENT_REF, returnedValue.getExternalShipmentReference());
        assertEquals(ARCHIVE_REF, returnedValue.getArchiveReference());
        assertEquals(RECIEVER_REF, returnedValue.getReceiversReference());
    }

    @Test
    public void updateReceipt_recepitstatus_REJECTED() {
        ObjectFactory objectFactory = new ObjectFactory();
        Receipt receipt = objectFactory.createReceipt();
        receipt.setReceiptStatus(ReceiptStatusEnum.REJECTED);

        SubmitFormTask submitFormTask = new SubmitFormTaskBuilder()
                .receipdId(RECEIPT_ID)
                .externalShipmentReference(EXT_SHIPMENT_REF)
                .build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            receiptService.updateReceipt(receipt, submitFormTask);
        });
    }

}