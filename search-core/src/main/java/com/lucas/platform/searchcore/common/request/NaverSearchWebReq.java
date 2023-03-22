package com.lucas.platform.searchcore.common.request;

import com.lucas.platform.searchcore.exception.BssErrorCode;
import com.lucas.platform.searchcore.exception.BssException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NaverSearchWebReq {


    private String query;

    private String sort;

    private int start;

    private int display;

    public static NaverSearchWebReq from(SearchWebReq searchWebReq) {
        return new NaverSearchWebReq(searchWebReq.getQuery(), sortMapper(searchWebReq.getSort()), searchWebReq.getPage(), searchWebReq.getSize());
    }

    private static String sortMapper(String sort) {
        return switch (sort) {
            case "accuracy" -> "sim";
            case "recency" -> "date";
            default -> throw new BssException(BssErrorCode.INTERNAL_SERVER_ERROR);
        };
    }

}
