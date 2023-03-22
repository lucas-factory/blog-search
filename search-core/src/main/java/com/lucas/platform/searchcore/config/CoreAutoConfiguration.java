package com.lucas.platform.searchcore.config;

import com.lucas.platform.searchcore.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CoreProperties.class)
public class CoreAutoConfiguration {

    public CoreAutoConfiguration(ApplicationContext context) {
        ContextUtil.setContext(context);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSource messageSource() {
        return new ReloadableResourceBundleMessageSource();
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(AbstractResourceBasedMessageSource messageSource) {
        MessageSourceProperties messageSourceProperties = new MessageSourceProperties();
        List<String> baseNameList = new ArrayList<>();

        String basename = messageSourceProperties.getBasename();
        if(StringUtils.hasText(basename)) {
            baseNameList.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(StringUtils.trimAllWhitespace(basename))));
        }
        messageSource.addBasenames(baseNameList.toArray(new String[0]));

        if (messageSourceProperties.getEncoding() != null) {
            messageSource.setDefaultEncoding(messageSourceProperties.getEncoding().name());
        }

        Duration cacheDuration = messageSourceProperties.getCacheDuration();
        if(cacheDuration != null) {
            messageSource.setCacheMillis(cacheDuration.toMillis());
        }

        messageSource.setFallbackToSystemLocale(messageSourceProperties.isFallbackToSystemLocale());
        messageSource.setAlwaysUseMessageFormat(messageSourceProperties.isAlwaysUseMessageFormat());
        messageSource.setUseCodeAsDefaultMessage(messageSourceProperties.isUseCodeAsDefaultMessage());

        return new MessageSourceAccessor(messageSource);
    }

}
