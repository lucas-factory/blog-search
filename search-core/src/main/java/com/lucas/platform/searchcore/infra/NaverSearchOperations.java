package com.lucas.platform.searchcore.infra;

import com.lucas.platform.searchcore.common.resopnse.NaverSearchRes;
import com.lucas.platform.searchcore.common.request.NaverSearchWebReq;
import com.lucas.platform.searchcore.common.request.SearchWebReq;
import com.lucas.platform.searchcore.common.resopnse.SearchWebRes;
import com.lucas.platform.searchcore.exception.BssErrorCode;
import com.lucas.platform.searchcore.exception.BssException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class NaverSearchOperations implements SearchOperations {

    private final WebClient naverClient;
    private final String publisher;

    private static final String SEARCH_WEB_URI = "/v1/search/blog.json?query={query}&sort={sort}&start={start}&display={display}";

    public NaverSearchOperations(String publisher, WebClient.Builder clientBuilder, String apiKey) {
        this.publisher = publisher;
        this.naverClient = buildClient(clientBuilder, apiKey);
    }

    @Override
    public Mono<SearchWebRes> searchWeb(SearchWebReq req) {
        NaverSearchWebReq nReq = NaverSearchWebReq.from(req);
        return naverClient.get()
                .uri(SEARCH_WEB_URI, nReq.getQuery(), nReq.getSort(), nReq.getStart(), nReq.getDisplay())
                .retrieve()
                .bodyToMono(NaverSearchRes.class)
                .map(NaverSearchRes::toSearchWebRes)
                .map(r -> r.withPublisher(publisher))
                .onErrorResume(WebClientResponseException.class, e -> Mono.error(new BssException(BssErrorCode.SearchClientResponseError, e)))
                .onErrorResume(WebClientRequestException.class, e -> Mono.error(new BssException(BssErrorCode.SearchClientRequestError, e)));
    }

    private WebClient buildClient(WebClient.Builder clientBuilder, String apiKey) {
        String[] split = apiKey.split(" ");
        String clientId = split[0];
        String secretId = split[1];
        return clientBuilder.defaultHeaders(headers -> {
                            headers.add("X-Naver-Client-Id", clientId);
                            headers.add("X-Naver-Client-Secret", secretId);
                        })
                .build();
    }

}
