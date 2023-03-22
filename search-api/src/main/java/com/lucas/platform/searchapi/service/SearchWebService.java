package com.lucas.platform.searchapi.service;

import com.lucas.platform.searchcore.common.domain.*;
import com.lucas.platform.searchcore.common.resopnse.KeywordCount;
import com.lucas.platform.searchcore.common.request.SearchWebReq;
import com.lucas.platform.searchcore.common.resopnse.SearchWebRes;
import com.lucas.platform.searchcore.common.resopnse.TopKeywordRes;
import com.lucas.platform.searchcore.config.SearchPublisherDiscovery;
import com.lucas.platform.searchapi.repository.KeywordRepository;
import com.lucas.platform.searchcore.exception.BssErrorCode;
import com.lucas.platform.searchcore.exception.BssException;
import com.lucas.platform.searchcore.infra.SearchOperations;
import jakarta.persistence.LockTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Transactional(readOnly = true)
@Service
public class SearchWebService {

    private final SearchPublisherDiscovery publisherDiscovery;

    private final KeywordRepository keywordRepository;

    SearchWebService(SearchPublisherDiscovery publisherDiscovery, KeywordRepository keywordRepository) {
        this.publisherDiscovery = publisherDiscovery;
        this.keywordRepository = keywordRepository;
    }

    @Transactional
    public SearchWebRes searchWeb(String web, SearchWebReq searchWebReq) throws InterruptedException {
        SearchOperations operations = publisherDiscovery.discover(web);
        List<SearchWebRes> response = new ArrayList<>();
        List<RuntimeException> exception = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        operations.searchWeb(searchWebReq)
                .map(response::add)
                .doOnError(e -> exception.add((RuntimeException) e))
                .doFinally(s -> latch.countDown())
                .subscribe();
        latch.await();
        if(response.isEmpty()) {
            throw exception.get(0);
        }
        saveKeyword(searchWebReq.getQuery());
        return response.get(0);
    }

    @Transactional
    public SearchWebRes searchWeb(SearchWebReq searchWebReq) throws InterruptedException {
        List<String> publishers = publisherDiscovery.publishers();
        log.info("publisher : {}", publishers);
        Map<String, SearchWebRes> responses = new HashMap<>();
        List<RuntimeException> exceptions = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(publishers.size());

        publishers.stream().map(publisherDiscovery::discover)
                .forEach(operations ->
                        operations.searchWeb(searchWebReq)
                                .doOnNext(r -> responses.put(r.getPublisher(), r))
                                .doOnError(e -> exceptions.add((RuntimeException) e))
                                .doFinally(s -> latch.countDown())
                                .subscribe()
                );

        latch.await();
        if(responses.isEmpty()) {
            throw exceptions.get(0);
        }
        saveKeyword(searchWebReq.getQuery());
        return publishers.stream()
                .filter(responses::containsKey)
                .map(responses::get)
                .findFirst()
                .orElseThrow();
    }

    public TopKeywordRes dailyTopKeywords(int top) {
        ZonedDateTime begin = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime end = begin.plusDays(1);
        List<KeywordCount> keywords = keywordRepository.topKeywords(top, begin, end);
        return TopKeywordRes.from(keywords, begin, end);

    }

    @Retryable(
            retryFor = {SQLException.class, LockTimeoutException.class},
            backoff = @Backoff(random = true, delay = 100, multiplier = 2),
            recover = "saveKeywordFailure"
    )
    public void saveKeyword(String text) {
        Keyword keyword = keywordRepository.keywordForUpdate(text)
                .orElse(new Keyword(text));

        if(keyword.isNew() || !isToday(keyword.getUpdatedAt())) {
            keywordRepository.save(new Keyword(text));
            return;
        }

        keyword.addCount();
        keywordRepository.save(keyword);
    }

    private boolean isToday(ZonedDateTime zonedDateTime) {
        return LocalDate.now(ZoneId.systemDefault()).equals(zonedDateTime.toLocalDate());
    }

    @Recover
    public void saveKeywordFailure(Exception e, String text) {
        log.debug("Failed to save keyword '{}'", text);
        throw new BssException(BssErrorCode.SAVE_KEYWORD_FAIL, e);
    }

}
