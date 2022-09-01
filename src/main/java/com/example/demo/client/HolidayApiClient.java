package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "holidayApi", url = "https://holidayapi.com")
public interface HolidayApiClient {

    @GetMapping("/v1/holidays")
    String getHolidays();
}
