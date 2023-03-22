package com.lucas.platform.searchcore.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileInfo {

    public static final String LOCAL = "local";
    public static final String TEST = "test";
    public static final String LIVE = "live";
    public static final String KR = "kr";
    public static final Set<String> NET_PROFILES = Set.of(LOCAL, TEST, LIVE);
    public static final Set<String> REGION_PROFILES = Set.of(KR);
    public static final String NET_PROFILE = "net_profile";
    public static final String REGION_PROFILE = "region_profile";

}
