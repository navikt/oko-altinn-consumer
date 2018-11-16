package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfForm;
import no.altinn.intermediaryinboundexternalec.Form;
import no.altinn.intermediaryinboundexternalec.FormTask;
import no.altinn.intermediaryinboundexternalec.ObjectFactory;
import no.nav.okonomi.altinn.consumer.formsubmitservice.AltinnFormSubmitConsumerConfig.PropertyMapKey;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;


@Service
class FormTaskService {

    private ObjectFactory objectFactory;

    private Map<PropertyMapKey, String> submitFormPropertyMap;

    @Inject
    public FormTaskService(Map<PropertyMapKey, String> submitFormPropertyMap) {
        this.objectFactory = new ObjectFactory();
        this.submitFormPropertyMap = submitFormPropertyMap;
    }

    FormTask createFormTask(AltinnMessage altinnMessage) {
        FormTask formTask = objectFactory.createFormTask();
        formTask.setServiceCode(submitFormPropertyMap.get(PropertyMapKey.SERVICE_CODE));
        formTask.setServiceEdition(Integer.valueOf(submitFormPropertyMap.get(PropertyMapKey.SERVICE_EDITION_CODE)));
        formTask.setForms(createForms(altinnMessage));

        return formTask;
    }

    private ArrayOfForm createForms(AltinnMessage altinnMessage) {
        ArrayOfForm arrayOfForm = objectFactory.createArrayOfForm();
        Form form = objectFactory.createForm();
        form.setCompleted(Boolean.TRUE);
        form.setDataFormatId(submitFormPropertyMap.get(PropertyMapKey.DATA_FORMAT_ID));
        form.setDataFormatVersion(Integer.valueOf(submitFormPropertyMap.get(PropertyMapKey.DATA_FORMAT_VERSION)));
        form.setEndUserSystemReference(altinnMessage.getOrderId());
        form.setParentReference(0);
        form.setFormData(altinnMessage.getFormData());
        arrayOfForm.getForm().add(form);

        return arrayOfForm;
    }

}
