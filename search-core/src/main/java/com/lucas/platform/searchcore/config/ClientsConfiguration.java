package com.lucas.platform.searchcore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.platform.searchcore.exception.BssErrorCode;
import com.lucas.platform.searchcore.exception.BssException;
import com.lucas.platform.searchcore.infra.KaKaoSearchOperations;
import com.lucas.platform.searchcore.infra.NaverSearchOperations;
import com.lucas.platform.searchcore.infra.SearchOperations;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
@EnableConfigurationProperties(ClientsProperties.class)
public class ClientsConfiguration {

    private final ClientsProperties clientsProperties;

    ClientsConfiguration(ClientsProperties clientsProperties) {
        this.clientsProperties = clientsProperties;
    }

    @Bean
    public SearchPublisherDiscovery publisherDiscovery(Function<String, HttpClient> httpClientBuilder, ObjectMapper mapper) {
        Map<String, SearchOperations> operationsMap = new LinkedHashMap<>();
        clientsProperties.getCredential().forEach((publisher, credential) -> {
            operationsMap.put(publisher, buildOperations(httpClientBuilder, mapper, publisher, credential));
        });
        return new SearchPublisherDiscovery(operationsMap);
    }

    public SearchOperations buildOperations(Function<String, HttpClient> httpClientBuilder, ObjectMapper mapper, String publisher, ClientsProperties.Credential credential) {
        HttpClient httpClient = httpClientBuilder.apply(credential.getBaseUri());
        WebClient.Builder webClientBuilder = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> {
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
                });
        return build(publisher, webClientBuilder, credential.getApiKey());
    }

    @Bean
    public Function<String, HttpClient> httpClientBuilder() {
        ClientsProperties.HttpClientProperties properties = clientsProperties.getHttpClient();
        return baseUrl -> {
            ConnectionProvider connectionProvider = ConnectionProvider.builder(baseUrl)
                    .maxConnections(properties.getMaxConnections())
                    .maxIdleTime(Duration.ofSeconds(properties.getMaxIdleTimeSec()))
                    .maxLifeTime(Duration.ofSeconds(properties.getMaxLifeTimeSec()))
                    .pendingAcquireTimeout(Duration.ofSeconds(properties.getPendingAcquireTimeoutSec()))
                    .lifo()
                    .build();
            return HttpClient.create(connectionProvider)
                    .baseUrl(baseUrl)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectionTimeoutMillis())
                    .option(ChannelOption.TCP_NODELAY, properties.isTcpNoDelay())
                    .option(ChannelOption.SO_KEEPALIVE, properties.isKeepAlive())
                    .responseTimeout(Duration.ofMillis(properties.getResponseTimeoutMillis()))
                    .doOnConnected(conn ->
                            conn.addHandlerLast(new ReadTimeoutHandler(properties.getReadTimeoutMillis()))
                                    .addHandlerLast(new WriteTimeoutHandler(properties.getWriteTimeoutMillis()))
                    );
        };
    }

    private SearchOperations build(String publisher, WebClient.Builder webClientBuilder, String apiKey) {
        return switch (publisher) {
            case "kakao" -> new KaKaoSearchOperations(publisher, webClientBuilder, apiKey);
            case "naver" -> new NaverSearchOperations(publisher, webClientBuilder, apiKey);
            default -> throw new BssException(BssErrorCode.INTERNAL_SERVER_ERROR);
        };
    }

}
