package com.lucas.platform.searchcore.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "search-modules.clients")
public class ClientsProperties {

    private Map<String, Credential> credential = new LinkedHashMap<>();
    private HttpClientProperties httpClient = new HttpClientProperties();

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class Credential {
        private String baseUri;
        private String apiKey;
    }

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class HttpClientProperties {
        private int connectionTimeoutMillis = 5000;
        private int responseTimeoutMillis = 15000;
        private int readTimeoutMillis = 10000;
        private int writeTimeoutMillis = 10000;

        private int maxConnections = 100;
        private int maxIdleTimeSec = 3;
        private int maxLifeTimeSec = 3;
        private int pendingAcquireTimeoutSec = 5;
        private boolean tcpNoDelay = true;
        private boolean keepAlive = true;
    }

}
