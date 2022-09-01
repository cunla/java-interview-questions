package com.example.demo.service;

import com.example.demo.client.HolidayApiClient;
import com.example.demo.entity.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HolidayService {

    HolidayApiClient holidayApiClient;

    @Value("${backbase.holiday.api.key}")
    private String apiKey;

    public HolidayService(HolidayApiClient holidayApiClient) {
        this.holidayApiClient = holidayApiClient;
    }

    public Response getHoliday(String country, String year, String month, String day) {
        return holidayApiClient.getHolidays(country, apiKey, year, month, day);
    }
}
