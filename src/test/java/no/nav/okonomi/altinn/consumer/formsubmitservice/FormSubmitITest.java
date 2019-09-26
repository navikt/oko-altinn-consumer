//package no.nav.okonomi.altinn.consumer.formsubmitservice;
//
//import no.altinn.intermediaryinboundexternalec.IIntermediaryInboundExternalEC;
//import no.nav.okonomi.altinn.consumer.SubmitFormTask;
//import org.apache.commons.io.IOUtils;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.inject.Inject;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.EnumMap;
//import java.util.Map;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.fail;
//
//@Ignore
//@ContextConfiguration(classes = {FormSubmitConsumerTestConfig.class})
//@RunWith(SpringJUnit4ClassRunner.class)
//@TestPropertySource({"classpath:properties/altinntjenester-uri.properties", "classpath:properties/navtestklient-credentials.properties", "classpath:properties/navtestklient-certificate.properties"})
//public class FormSubmitITest {
//
//    private static final String SERVICE_CODE = "3103";
//    private static final String SERVICE_EDITION_CODE = "170424";
//    private static final String DATA_FORMAT_ID = "1548";
//    private static final String DATA_FORMAT_VERSION = "11936";
//    private static final String LANGUAGE_ID = "1044";
//    private static final String DATA_FORMAT_PROVIDER = "SERES";
//
//
//    @Inject
//    private IIntermediaryInboundExternalEC formSubmitPortType;
//
//    private FormTaskShipmentService formTaskShipmentService;
//
//    private AltinnFormSubmitConsumerService service;
//
//    @Before
//    public void setup() {
//        FormSubmitServiceProperties serviceProperties = new FormSubmitServiceProperties(SERVICE_CODE,
//                SERVICE_EDITION_CODE, DATA_FORMAT_ID, DATA_FORMAT_VERSION);
//        AttachmentService attachmentService = new AttachmentService();
//        FormTaskService formTaskService = new FormTaskService(serviceProperties);
//        formTaskShipmentService = new FormTaskShipmentService(attachmentService, formTaskService);
//
//        service = new DefaultAltinnFormSubmitConsumerService(formSubmitPortType, FormSubmitConsumerTestConfig.credentials, formTaskShipmentService);
//    }
//
//    @Test
//    public void formSubmitServiceTest() {
//        try {
//            service.test();
//        } catch (Exception e) {
//            fail("Caught exception");
//        }
//    }
//
//    @Test
//    public void testSubmitFormTaskWithAttachment() throws IOException {
//        InputStream forespoersel = new ClassPathResource("attachments/forespoerselOmESkattekort.xml").getInputStream();
//
//        RF1211MessageVO rf1211 = new RF1211MessageBuilder()
//                .orderId("90f70a46-cb4e-4dcc-9138-45bc3fdf8f92")
//                .orgnummer("910962728")
//                .navn("SUNDEBRU OG FJØRTOFT REVISJON")
//                .inntektsaar("2017")
//                .vedlegg(IOUtils.toByteArray(forespoersel))
//                .skattekortVarslingstype(SkattekortVarslingstype.VARSEL_VED_FOERSTE_SKATTEKORTENDRING)
//                .skattekortMeldingstype(SkattekortMeldingstype.INNSENDING_MED_VEDLEGG_UTEN_ENDRINGER)
//                .skattekortHenteEndredeSkattekort(SkattekortHenteEndredeSkattekort.SIDEN_SISTE_FORESPOERSEL)
//                .build();
//
////        SubmitFormTask sft = service.submitForm(rf1211);
////        assertNotNull(sft);
//    }
//
//}