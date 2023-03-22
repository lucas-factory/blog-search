package com.lucas.platform.searchcore.common.resopnse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchWebRes {

    private Metadata meta;
    private List<Document> documents;

    public SearchWebRes(Metadata meta, List<Document> documents) {
        this.meta = meta;
        this.documents = documents;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class Metadata {
        private Integer totalCount;
        private Integer pageableCount;
        private Boolean isEnd;
        private String publisher;

        public Metadata(int totalCount, int pageableCount, boolean isEnd) {
            this.totalCount = totalCount;
            this.pageableCount = pageableCount;
            this.isEnd = isEnd;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class Document {
        private String title;
        private String contents;
        private String url;
        private ZonedDateTime datetime;

        public Document(String title, String contents, String url, ZonedDateTime datetime) {
            this.title = title;
            this.contents = contents;
            this.url = url;
            this.datetime = datetime;
        }
    }

    public SearchWebRes withPublisher(String publisher) {
        meta.publisher = publisher;
        return this;
    }

    public String getPublisher() {
        return meta.publisher;
    }

}
