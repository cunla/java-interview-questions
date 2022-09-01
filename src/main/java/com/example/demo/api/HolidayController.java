package com.example.demo.api;

import com.example.demo.dto.NearestHoliday;
import com.example.demo.entity.Response;
import com.example.demo.mapper.HolidayResponseMapper;
import com.example.demo.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
public class HolidayController {

    @Autowired
    HolidayService holidayService;

    @Autowired
    HolidayResponseMapper mapper;

    @GetMapping("/holiday")
    @ResponseBody
    public ResponseEntity<NearestHoliday> getNearestHoliday(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                            @RequestParam String country) {
        Response response = holidayService.getHoliday(country, String.valueOf(date.getYear()),
                String.valueOf(date.getMonthValue()),
                String.valueOf(date.getDayOfMonth()));
        NearestHoliday holiday = mapper.convert(response);
        return ResponseEntity.ok(holiday);
    }

}