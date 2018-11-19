package no.nav.okonomi.altinn.consumer.formsubmitservice;

import no.altinn.intermediaryinboundexternalec.ArrayOfForm;
import no.altinn.intermediaryinboundexternalec.Form;
import no.altinn.intermediaryinboundexternalec.FormTask;
import no.altinn.intermediaryinboundexternalec.ObjectFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Service
class FormTaskService {

    private ObjectFactory objectFactory;
    private FormSubmitServiceProperties formSubmitServiceProps;

    @Inject
    public FormTaskService(FormSubmitServiceProperties formSubmitServiceProps) {
        this.objectFactory = new ObjectFactory();
        this.formSubmitServiceProps = formSubmitServiceProps;
    }

    FormTask createFormTask(AltinnMessage altinnMessage) {
        FormTask formTask = objectFactory.createFormTask();
        formTask.setServiceCode(formSubmitServiceProps.getServiceCode());
        formTask.setServiceEdition(Integer.valueOf(formSubmitServiceProps.getServiceEditionCode()));
        formTask.setForms(createForms(altinnMessage));

        return formTask;
    }

    private ArrayOfForm createForms(AltinnMessage altinnMessage) {
        ArrayOfForm arrayOfForm = objectFactory.createArrayOfForm();
        Form form = objectFactory.createForm();
        form.setCompleted(Boolean.TRUE);
        form.setDataFormatId(formSubmitServiceProps.getDataFormatId());
        form.setDataFormatVersion(Integer.valueOf(formSubmitServiceProps.getDataFormatVersion()));
        form.setEndUserSystemReference(altinnMessage.getOrderId());
        form.setParentReference(0);
        form.setFormData(altinnMessage.getFormData());
        arrayOfForm.getForm().add(form);

        return arrayOfForm;
    }

}
