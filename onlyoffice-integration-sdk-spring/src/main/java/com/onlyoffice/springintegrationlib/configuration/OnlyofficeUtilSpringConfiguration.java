package com.onlyoffice.springintegrationlib.configuration;

import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeUtilSpringConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeFile onlyofficeFileUtil() {
        return new OnlyofficeFileUtil();
    }

    @ConditionalOnMissingBean
    @Bean
    public OnlyofficeConfig onlyofficeConfigUtil(OnlyofficeFile fileUtil) {
        return new OnlyofficeConfigUtil(fileUtil);
    }
}
