package com.onlyoffice.configuration;

import base.util.OnlyofficeConfigUtil;
import base.util.OnlyofficeFileUtil;
import core.util.OnlyofficeConfig;
import core.util.OnlyofficeFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyofficeUtilSpringConfiguration {
    @Bean
    public OnlyofficeFile onlyofficeFileUtil() {
        return new OnlyofficeFileUtil();
    }

    @Bean
    public OnlyofficeConfig onlyofficeConfigUtil(OnlyofficeFile fileUtil) {
        return new OnlyofficeConfigUtil(fileUtil);
    }
}
