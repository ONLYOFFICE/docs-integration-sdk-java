package com.onlyoffice.springintegrationlib.configuration;

import core.util.OnlyofficeConfigUtil;
import core.util.OnlyofficeConfigUtilBase;
import core.util.OnlyofficeFileUtil;
import core.util.OnlyofficeFileUtilBase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeUtilSpringConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeFileUtil onlyofficeFileUtil() {
        return new OnlyofficeFileUtilBase();
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeConfigUtil onlyofficeConfigUtil(OnlyofficeFileUtil fileUtil) {
        return new OnlyofficeConfigUtilBase(fileUtil);
    }
}
