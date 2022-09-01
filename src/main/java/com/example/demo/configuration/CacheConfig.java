package com.example.demo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import javax.management.timer.Timer;

@EnableCaching
@Configuration
@Slf4j
public class CacheConfig {

    public static final String CACHE_NAME = "holidays";

    private static final long ONE_DAY = Timer.ONE_HOUR * 24;

    @Scheduled(fixedRate = ONE_DAY)
    @CacheEvict(
            value = {CACHE_NAME},
            allEntries = true)
    public void clearGroups() {
        log.info("Evict all holidays results.");
    }
}
