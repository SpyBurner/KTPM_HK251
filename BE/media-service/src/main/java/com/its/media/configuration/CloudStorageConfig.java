package com.its.media.configuration;

public interface CloudStorageConfig {

    void configure();

    boolean isEnabled();

    String getProviderName();
}