package com.lucas.platform.searchapi.repository;

import com.lucas.platform.searchcore.common.domain.Keyword;
import com.lucas.platform.searchcore.common.resopnse.KeywordCount;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class KeywordRepository {

    private final KeywordJpaRepository jpaRepository;

    public KeywordRepository(KeywordJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public List<KeywordCount> topKeywords(int top, ZonedDateTime begin, ZonedDateTime end) {
        return jpaRepository.findTopCount(PageRequest.of(0, top), begin, end).getContent();
    }

    public Optional<Keyword> keywordForUpdate(String text) {
        return jpaRepository.findByTextForUpdate(text);
    }

    public Keyword save(Keyword keyword) {
        return jpaRepository.save(keyword);
    }

}
