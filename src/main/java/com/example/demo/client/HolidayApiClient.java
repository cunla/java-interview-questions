package com.example.demo.client;

import com.example.demo.configuration.OkHttpFeignConfiguration;
import com.example.demo.entity.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "holidayApi", url = "https://holidayapi.com", configuration = OkHttpFeignConfiguration.class)
public interface HolidayApiClient {

    @GetMapping("/v1/holidays")
    Response getHolidays(@RequestParam(name = "country") String country,
                         @RequestParam(name = "key") String key,
                         @RequestParam(name = "year") String year,
                         @RequestParam(name = "month") String month,
                         @RequestParam(name = "day") String day,
                         @RequestParam(name = "upcoming", defaultValue = "true") String upcoming);
}
