package com.lucas.platform.searchcore.infra;

import com.lucas.platform.searchcore.common.request.SearchWebReq;
import com.lucas.platform.searchcore.common.resopnse.SearchWebRes;
import reactor.core.publisher.Mono;

public interface SearchOperations {

    Mono<SearchWebRes> searchWeb(SearchWebReq req);

}
