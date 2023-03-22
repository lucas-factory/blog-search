package com.lucas.platform.searchapi.controller;

import com.lucas.platform.searchcore.annotation.PublisherType;
import com.lucas.platform.searchcore.common.request.SearchWebReq;
import com.lucas.platform.searchcore.common.resopnse.SearchWebRes;
import com.lucas.platform.searchapi.service.SearchWebService;
import com.lucas.platform.searchcore.common.resopnse.TopKeywordRes;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/bss")
public class SearchWebController {

    private final SearchWebService searchWebService;

    SearchWebController(SearchWebService searchWebService) {
        this.searchWebService = searchWebService;
    }

    @GetMapping("/v1/search/web")
    public SearchWebRes searchWeb(@Valid SearchWebReq searchWebReq) throws InterruptedException {
        return searchWebService.searchWeb(searchWebReq);
    }

    @GetMapping("/v1/search/web/{publisher}")
    public SearchWebRes searchWeb(@PathVariable @PublisherType String publisher, @Valid SearchWebReq searchWebReq) throws InterruptedException {
        return searchWebService.searchWeb(publisher, searchWebReq);
    }

    @GetMapping("/v1/search/keywords/top10/daily")
    public TopKeywordRes dailyTop10Keywords() {
        return searchWebService.dailyTopKeywords(10);
    }

}
