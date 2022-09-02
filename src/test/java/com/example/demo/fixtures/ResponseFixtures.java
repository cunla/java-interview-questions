package com.example.demo.fixtures;

import com.example.demo.dto.NearestHoliday;
import com.example.demo.entity.Holiday;
import com.example.demo.entity.Response;

import java.util.ArrayList;
import java.util.List;

public class ResponseFixtures {
    private static String DATE = "2021-07-04";
    private static String NAME = "National";

    public static Response generate() {
        final Response response = new Response();
        response.setStatus(200);
        List<Holiday> list = new ArrayList<>();
        final Holiday holiday = new Holiday();
        holiday.setName(NAME);
        holiday.setDate(DATE);
        list.add(holiday);
        response.setHolidays(list);
        return response;
    }

    public static NearestHoliday generateNearest() {
        NearestHoliday nearestHoliday = new NearestHoliday();
        nearestHoliday.setNearestHolidayDate(DATE);
        nearestHoliday.setHolidayName(NAME);
        return nearestHoliday;
    }
}
