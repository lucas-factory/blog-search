package com.lucas.platform.searchcore.common.resopnse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TopKeywordRes {

    private Metadata meta;

    private List<Document> documents;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class Metadata {

        private Integer totalCount;
        private ZonedDateTime begin;
        private ZonedDateTime end;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class Document {

        private String keyword;
        private Long count;

    }

    public static TopKeywordRes from(List<KeywordCount> keywords, ZonedDateTime begin, ZonedDateTime end) {
        Metadata meta = new Metadata(keywords.size(), begin, end);
        List<Document> documents = new ArrayList<>();
        for(KeywordCount keyword : keywords) {
            documents.add(new Document(keyword.getText(), keyword.getCount()));
        }
        return new TopKeywordRes(meta, documents);
    }

}
