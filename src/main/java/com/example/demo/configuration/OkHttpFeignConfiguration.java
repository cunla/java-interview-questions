package com.example.demo.configuration;

import com.example.demo.client.FeignErrorDecoder;
import feign.Contract;
import feign.codec.ErrorDecoder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Configuration(
        proxyBeanMethods = false
)
@ConditionalOnMissingBean({OkHttpClient.class})
public class OkHttpFeignConfiguration {
    private OkHttpClient okHttpClient;

    public OkHttpFeignConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean({ConnectionPool.class})
    public ConnectionPool httpClientConnectionPool(FeignHttpClientProperties httpClientProperties,
                                                   OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        int maxTotalConnections = httpClientProperties.getMaxConnections();
        long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    public OkHttpClient client(OkHttpClientFactory httpClientFactory, ConnectionPool connectionPool,
                               FeignHttpClientProperties httpClientProperties) {
        boolean followRedirects = httpClientProperties.isFollowRedirects();
        int connectTimeout = httpClientProperties.getConnectionTimeout();
        this.okHttpClient = httpClientFactory.createBuilder(false)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .build();
        return this.okHttpClient;
    }

    @Bean
    public Contract useFeignAnnotation() {
        return new Contract.Default();
    }

    @Bean
    public ErrorDecoder errorDecoder(){
        return new FeignErrorDecoder();
    }

    @PreDestroy
    public void destroy() {
        if (this.okHttpClient != null) {
            this.okHttpClient.dispatcher().executorService().shutdown();
            this.okHttpClient.connectionPool().evictAll();
        }
    }
}
