package com.example.demo.mapper;

import com.example.demo.dto.NearestHoliday;
import com.example.demo.entity.Holiday;
import com.example.demo.entity.Response;

public class HolidayResponseMapper {
    public NearestHoliday convert(Response response) {
        NearestHoliday nearestHoliday = new NearestHoliday();
        Holiday holiday = response.getHolidays().stream().findFirst().orElseThrow();
        nearestHoliday.setNearestHolidayDate(holiday.getDate());
        nearestHoliday.setHolidayName(holiday.getName());
        return nearestHoliday;
    }
}
