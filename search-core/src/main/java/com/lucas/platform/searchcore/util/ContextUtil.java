package com.lucas.platform.searchcore.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextUtil {

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        ContextUtil.context = context;
    }

}
