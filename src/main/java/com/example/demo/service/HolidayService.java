package com.example.demo.service;

import com.example.demo.client.HolidayApiClient;
import com.example.demo.entity.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.example.demo.configuration.CacheConfig.CACHE_NAME;

@Service
@Slf4j
public class HolidayService {

    @Value("${backbase.holiday.api.key}")
    private String apiKey;

    HolidayApiClient holidayApiClient;

    public HolidayService(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;
    }

    @Cacheable(value = CACHE_NAME, key = "{#country, #year,#month, #day}")
    public Response getHoliday(String country, String year, String month, String day) {
        if (!country.equals("US")) {
            throw new IllegalArgumentException("Currently only supported US country, please use US !");
        }
        return holidayApiClient.getHolidays(country, apiKey, year, month, day);
    }
}
