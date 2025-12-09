package com.its.media.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CloudStorageConfigInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final List<CloudStorageConfig> cloudStorageConfigs;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("========================================");
        log.info("Cloud Storage Configuration");
        log.info("========================================");

        if (cloudStorageConfigs.isEmpty()) {
            log.warn("No cloud storage provider configured!");
            return;
        }

        cloudStorageConfigs.forEach(config -> {
            if (config.isEnabled()) {
                log.info("Active Provider: {}", config.getProviderName());
                config.configure();
            }
        });

        log.info("========================================");
    }
}