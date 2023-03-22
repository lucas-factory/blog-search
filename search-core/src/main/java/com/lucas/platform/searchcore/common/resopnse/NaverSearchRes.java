package com.lucas.platform.searchcore.common.resopnse;

import com.lucas.platform.searchcore.util.DateTimeParser;
import lombok.Getter;

import java.util.List;

@Getter
public class NaverSearchRes {

    private Integer total;
    private Integer start;
    private Integer display;

    private List<Item> items;

    @Getter
    public static class Item {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private String postdate;
    }

    final static int MAX_PAGE = 50;

    public SearchWebRes toSearchWebRes() {
        int totalRead = start * display;
        int maxRead = MAX_PAGE * display;
        int pageableCount = maxRead - totalRead;
        boolean isEnd = totalRead >= maxRead;
        SearchWebRes.Metadata meta = new SearchWebRes.Metadata(total, pageableCount, isEnd);
        List<SearchWebRes.Document> documents = items.stream()
                .map(item -> new SearchWebRes.Document(item.title, item.description, item.link, DateTimeParser.parseNaverDate(item.postdate)))
                .toList();
        return new SearchWebRes(meta, documents);
    }
}
