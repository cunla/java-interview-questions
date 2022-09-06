package com.example.demo.client;

import com.example.demo.configuration.OkHttpFeignConfiguration;
import com.example.demo.entity.Response;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "holidayApi", url = "${base.url}", configuration = OkHttpFeignConfiguration.class)
public interface HolidayApiClient {

    @RequestLine("GET /v1/holidays?country={country}&key={key}&year={year}&month={month}&day={day}&upcoming=true")
    Response getHolidays(@Param("country") String country,
                         @Param("key") String key,
                         @Param("year") String year,
                         @Param("month") String month,
                         @Param("day") String day);
}
