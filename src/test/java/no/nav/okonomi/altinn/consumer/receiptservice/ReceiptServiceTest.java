//package no.nav.okonomi.altinn.consumer.receiptservice;
//
//import no.altinn.receiptexternalec.ReceiptStatusEnum;
//import no.altinn.receiptexternalec.ReferenceType;
//import no.altinn.receiptexternalec.v201506.ObjectFactory;
//import no.altinn.receiptexternalec.v201506.Receipt;
//import no.altinn.receiptexternalec.v201506.ReceiptSearch;
//import no.altinn.receiptexternalec.v201506.Reference;
//import no.altinn.receiptexternalec.v201506.ReferenceList;
//import no.nav.okonomi.altinn.consumer.SubmitFormTask;
//import no.nav.okonomi.altinn.consumer.SubmitFormTaskBuilder;
//import org.junit.Test;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//
//public class ReceiptServiceTest {
//
//    private ReceiptService receiptService = new ReceiptService();
//
//    private static final String ARCHIVE_REF = "archiveRef";
//    private static final String RECIEVER_REF = "recieverRef";
//    private static final Integer RECEIPT_ID = 1;
//    private static final String EXT_SHIPMENT_REF = "extShipmentRef";
//
//    @Test
//    public void createReceipt() {
//        Integer receiptId = 1;
//        String extShipmentReference = "extShipmentReference";
//
//        ReceiptSearch receiptSearch = receiptService.createReceipt(receiptId, extShipmentReference);
//
//        assertThat(receiptSearch.getReceiptId(), is(receiptId));
//        assertThat(receiptSearch.getReferences().getValue().getReference().size(), is(1));
//        assertThat(receiptSearch.getReferences().getValue().getReference().get(0).getReferenceType(), is(ReferenceType.EXTERNAL_SHIPMENT_REFERENCE));
//        assertThat(receiptSearch.getReferences().getValue().getReference().get(0).getReferenceValue(), is(extShipmentReference));
//    }
//
//    @Test
//    public void updateReceipt_recepitstatus_OK() {
//        ObjectFactory objectFactory = new ObjectFactory();
//        Receipt receipt = objectFactory.createReceipt();
//        receipt.setReceiptStatus(ReceiptStatusEnum.OK);
//
//        Reference referenceArchiveRef = objectFactory.createReference();
//        referenceArchiveRef.setReferenceType(ReferenceType.ARCHIVE_REFERENCE);
//        referenceArchiveRef.setReferenceValue(ARCHIVE_REF);
//
//        Reference referenceRecieverRef = objectFactory.createReference();
//        referenceRecieverRef.setReferenceType(ReferenceType.RECEIVERS_REFERENCE);
//        referenceRecieverRef.setReferenceValue(RECIEVER_REF);
//
//        ReferenceList referenceList = new ReferenceList();
//        referenceList.getReference().add(referenceArchiveRef);
//        referenceList.getReference().add(referenceRecieverRef);
//
//        receipt.setReferences(objectFactory.createReferenceList(referenceList));
//
//        SubmitFormTask submitFormTask = new SubmitFormTaskBuilder()
//                .receipdId(RECEIPT_ID)
//                .externalShipmentReference(EXT_SHIPMENT_REF)
//                .build();
//
//        SubmitFormTask returnedValue = receiptService.updateReceipt(receipt, submitFormTask);
//
//        assertThat(returnedValue.getReceiptId(), is(RECEIPT_ID));
//        assertThat(returnedValue.getExternalShipmentReference(), is(EXT_SHIPMENT_REF));
//        assertThat(returnedValue.getArchiveReference(), is(ARCHIVE_REF));
//        assertThat(returnedValue.getReceiversReference(), is(RECIEVER_REF));
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void updateReceipt_recepitstatus_REJECTED() {
//        ObjectFactory objectFactory = new ObjectFactory();
//        Receipt receipt = objectFactory.createReceipt();
//        receipt.setReceiptStatus(ReceiptStatusEnum.REJECTED);
//
//        SubmitFormTask submitFormTask = new SubmitFormTaskBuilder()
//                .receipdId(RECEIPT_ID)
//                .externalShipmentReference(EXT_SHIPMENT_REF)
//                .build();
//
//        receiptService.updateReceipt(receipt, submitFormTask);
//    }
//
//}