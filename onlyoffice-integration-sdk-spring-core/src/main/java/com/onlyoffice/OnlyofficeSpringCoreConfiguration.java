package com.onlyoffice;

import com.onlyoffice.configuration.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        OnlyofficeCallbackHandlersSpringConfiguration.class, OnlyofficeClientSpringConfiguration.class,
        OnlyofficeConverterHandlersSpringConfiguration.class, OnlyofficeCoreSpringConfiguration.class,
        OnlyofficeSecuritySpringConfiguration.class, OnlyofficeThirdPartySpringConfiguration.class,
        OnlyofficeUtilSpringConfiguration.class
})
public class OnlyofficeSpringCoreConfiguration { }
