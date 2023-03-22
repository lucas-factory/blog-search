package com.lucas.platform.searchcore.config;

import com.lucas.platform.searchcore.common.ProfileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;
import java.util.Set;

@Slf4j
public class ProfilesEnvironmentProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String[] activeProfiles = environment.getActiveProfiles();

        Properties properties = new Properties();
        Set<String> netProfiles = ProfileInfo.NET_PROFILES;
        Set<String> regionProfiles = ProfileInfo.REGION_PROFILES;

        // default net - local, region - kr
        properties.setProperty(ProfileInfo.NET_PROFILE, ProfileInfo.LOCAL);
        properties.setProperty(ProfileInfo.REGION_PROFILE, ProfileInfo.KR);

        for (String profile : activeProfiles) {
            if (netProfiles.contains(profile)) {
                properties.setProperty(ProfileInfo.NET_PROFILE, profile);
            } else if (regionProfiles.contains(profile)) {
                properties.setProperty(ProfileInfo.REGION_PROFILE, profile);
            }
        }

        environment.getPropertySources().addFirst(new PropertiesPropertySource("bssProperties", properties));
    }

}
