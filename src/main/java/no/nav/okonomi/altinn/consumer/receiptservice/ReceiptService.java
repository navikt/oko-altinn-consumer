package no.nav.okonomi.altinn.consumer.receiptservice;

import no.altinn.receiptexternalec.ReceiptStatusEnum;
import no.altinn.receiptexternalec.ReferenceType;
import no.altinn.receiptexternalec.v201506.ObjectFactory;
import no.altinn.receiptexternalec.v201506.Receipt;
import no.altinn.receiptexternalec.v201506.ReceiptSearch;
import no.altinn.receiptexternalec.v201506.Reference;
import no.altinn.receiptexternalec.v201506.ReferenceList;
import no.nav.okonomi.altinn.consumer.SubmitFormTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;

public class ReceiptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiptService.class);

    private ObjectFactory objectFactory;

    public ReceiptService() {
        this.objectFactory = new ObjectFactory();
    }

    ReceiptSearch createReceipt(Integer receiptId, String externalShipmentReference) {
        Reference reference = objectFactory.createReference();
        reference.setReferenceType(ReferenceType.EXTERNAL_SHIPMENT_REFERENCE);
        reference.setReferenceValue(externalShipmentReference);

        return createReceiptSearch(receiptId, reference);
    }

    private ReceiptSearch createReceiptSearch(Integer receipdId, Reference reference) {
        ReceiptSearch receiptSearch = objectFactory.createReceiptSearch();
        receiptSearch.setReceiptId(receipdId);

        ReferenceList referenceList = objectFactory.createReferenceList();
        referenceList.getReference().add(reference);

        receiptSearch.setReferences(objectFactory.createReceiptSaveReferences(referenceList));

        return receiptSearch;
    }

    SubmitFormTask updateReceipt(Receipt receipt, SubmitFormTask submitFormTask) {
        if (ReceiptStatusEnum.OK == receipt.getReceiptStatus()) {
            String archiveReference = submitFormTask.getArchiveReference();
            String receiversReference = submitFormTask.getReceiversReference();
            for (Reference ref : receipt.getReferences().getValue().getReference()) {
                if (ReferenceType.ARCHIVE_REFERENCE == ref.getReferenceType()) {
                    archiveReference = ref.getReferenceValue();
                } else if (ReferenceType.RECEIVERS_REFERENCE == ref.getReferenceType()) {
                    receiversReference = ref.getReferenceValue();
                }
            }
            String externalShipmentReference = submitFormTask.getExternalShipmentReference();
            return new SubmitFormTask(submitFormTask.getReceiptId(),
                    externalShipmentReference, archiveReference, receiversReference);
        } else {
            StringBuilder sb = new StringBuilder("ReceiptStatus er ikke OK, var ")
                    .append(receipt.getReceiptStatus())
                    .append(" (")
                    .append(receipt.getReceiptText())
                    .append(')');

            String subReceiptText = getSubReceiptText(receipt);
            if (!isAnyEmpty(subReceiptText)) {
                sb.append("(subReceiptText: ").append(subReceiptText).append(')');
            }

            LOGGER.warn("Kvittering med status {} mottatt fra Altinn: {}", receipt.getReceiptStatus(), sb);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    private String getSubReceiptText(Receipt receipt) {
        if (receipt.getSubReceipts() != null && !receipt.getSubReceipts().getValue().getReceipt().isEmpty()) {
            return receipt.getSubReceipts().getValue().getReceipt().get(0).getReceiptText().getValue();
        } else {
            return "";
        }
    }

    private static boolean isAnyEmpty(final CharSequence... seq) {
        if ((seq == null ? 0 : Array.getLength(seq)) == 0) {
            return true;
        }
        for (final CharSequence cs : seq) {
            if (cs == null || cs.length() == 0) {
                return true;
            }
        }
        return false;
    }

}
