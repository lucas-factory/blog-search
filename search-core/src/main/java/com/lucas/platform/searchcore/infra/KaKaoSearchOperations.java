package com.lucas.platform.searchcore.infra;

import com.lucas.platform.searchcore.common.resopnse.SearchWebRes;
import com.lucas.platform.searchcore.common.request.SearchWebReq;
import com.lucas.platform.searchcore.exception.BssErrorCode;
import com.lucas.platform.searchcore.exception.BssException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class KaKaoSearchOperations implements SearchOperations {

    private final WebClient kaKaoClient;
    private final String publisher;

    private static final String SEARCH_WEB_URI = "/v2/search/web?query={query}&sort={sort}&page={page}&size={size}";

    public KaKaoSearchOperations(String publisher, WebClient.Builder clientBuilder, String apiKey) {
        this.publisher = publisher;
        this.kaKaoClient = buildClient(clientBuilder, apiKey);
    }

    @Override
    public Mono<SearchWebRes> searchWeb(SearchWebReq req) {
        return kaKaoClient.get()
                .uri(SEARCH_WEB_URI, req.getQuery(), req.getSort(), req.getPage(), req.getSize())
                .retrieve()
                .bodyToMono(SearchWebRes.class)
                .map(r -> r.withPublisher(publisher))
                .onErrorResume(WebClientResponseException.class, e -> Mono.error(new BssException(BssErrorCode.SearchClientResponseError, e)))
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new BssException(BssErrorCode.SearchClientRequestError, e)));
    }

    private WebClient buildClient(WebClient.Builder clientBuilder, String apiKey) {
        return clientBuilder.defaultHeader("Authorization", apiKey)
                .build();
    }

}
