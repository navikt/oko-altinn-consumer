package no.nav.okonomi.altinn.consumer.formsubmitservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.EnumMap;
import java.util.Map;

@Configuration
@Import(value = {AltinnIntermediaryInboundConsumerConfig.class})
public class AltinnFormSubmitConsumerConfig {

    @Value("${nav.altinn-consumer.formsubmitservice.servicecode:3103}")
    private String rf1211ServiceCode;

    @Value("${nav.altinn-consumer.formsubmitservice.serviceeditioncode:170424}")
    private String rf1211ServiceEditionCode;

    @Value("${nav.altinn-consumer.formsubmitservice.dataformatid:1548}")
    private String rf1211DataFormatId;

    @Value("${nav.altinn-consumer.formsubmitservice.dataformatversion:11936}")
    private String rf1211DataFormatVersion;

    @Value("${nav.altinn-consumer.languageid:1044}")
    private String languageId;

    @Value("${nav.altinn-consumer.formsubmitservice.dataformatprovider:SERES}")
    private String dataFormatProvider;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Map<PropertyMapKey, String> submitFormPropertyMap() {
        Map<PropertyMapKey, String> map = new EnumMap<>(PropertyMapKey.class);
        map.put(PropertyMapKey.SERVICE_CODE, rf1211ServiceCode);
        map.put(PropertyMapKey.SERVICE_EDITION_CODE, rf1211ServiceEditionCode);
        map.put(PropertyMapKey.DATA_FORMAT_ID, rf1211DataFormatId);
        map.put(PropertyMapKey.DATA_FORMAT_VERSION, rf1211DataFormatVersion);
        map.put(PropertyMapKey.LANGUAGE_ID, languageId);
        map.put(PropertyMapKey.DATA_FORMAT_PROVIDER, dataFormatProvider);

        return map;
    }

    public enum PropertyMapKey {
        SERVICE_CODE,
        SERVICE_EDITION_CODE,
        DATA_FORMAT_ID,
        DATA_FORMAT_VERSION,
        LANGUAGE_ID,
        DATA_FORMAT_PROVIDER
    }

}