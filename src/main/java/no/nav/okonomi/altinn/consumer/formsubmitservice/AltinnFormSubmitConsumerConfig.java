package no.nav.okonomi.altinn.consumer.formsubmitservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {AltinnIntermediaryInboundConsumerConfig.class})
public class AltinnFormSubmitConsumerConfig {

    @Value("${altinn-consumer.formsubmitservice.servicecode:3103}")
    private String rf1211ServiceCode;

    @Value("${altinn-consumer.formsubmitservice.serviceeditioncode:170424}")
    private String rf1211ServiceEditionCode;

    @Value("${altinn-consumer.formsubmitservice.dataformatid:1548}")
    private String rf1211DataFormatId;

    @Value("${altinn-consumer.formsubmitservice.dataformatversion:11936}")
    private String rf1211DataFormatVersion;

    @Bean
    public FormSubmitServiceProperties formSubmitServiceProperties() {
        return new FormSubmitServiceProperties(
                rf1211ServiceCode,
                rf1211ServiceEditionCode,
                rf1211DataFormatId,
                rf1211DataFormatVersion);
    }

}