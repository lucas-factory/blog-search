package com.lucas.platform.searchcore.config;

import com.lucas.platform.searchcore.exception.BssErrorCode;
import com.lucas.platform.searchcore.exception.BssException;
import com.lucas.platform.searchcore.infra.SearchOperations;

import java.util.List;
import java.util.Map;

public class SearchPublisherDiscovery {

    private final Map<String, SearchOperations> operationsMap;

    public SearchPublisherDiscovery(Map<String, SearchOperations> operationsMap) {
        this.operationsMap = operationsMap;
    }

    public SearchOperations discover(String publisher) {
        if(!operationsMap.containsKey(publisher)) {
            throw new BssException(BssErrorCode.NOT_SUPPORTED_PUBLISHER);
        }
        return operationsMap.get(publisher);
    }

    public List<String> publishers() {
        return operationsMap.keySet().stream().toList();
    }

}
