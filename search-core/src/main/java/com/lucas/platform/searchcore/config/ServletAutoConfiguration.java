package com.lucas.platform.searchcore.config;

import com.lucas.platform.searchcore.exception.ServletGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(CoreAutoConfiguration.class)
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ServletAutoConfiguration {

    @Bean
    public ServletGlobalExceptionHandler servletExceptionHandler(MessageSourceAccessor messageSourceAccessor, CoreProperties coreProperties) {
        return new ServletGlobalExceptionHandler(messageSourceAccessor, coreProperties);
    }

}
