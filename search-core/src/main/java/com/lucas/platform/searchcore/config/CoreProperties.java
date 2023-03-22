package com.lucas.platform.searchcore.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ConfigurationProperties(prefix = "search-modules.core")
public class CoreProperties {

    private int productId;
    private int moduleId;
    private Validation validation = new Validation();

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Validation {
        private Set<String> sortTypes = Set.of("accuracy", "recency");
    }

}