package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolidayController {

    @Value("${backbase.holiday.api.key}")
    private String apiKey;

    @GetMapping("/holiday")
    @ResponseBody
    public ResponseEntity<String> getNearestHoliday(@RequestParam(required = false) String date,
                                            @RequestParam String country) {

        return ResponseEntity.ok(country + ", " + date);
    }

}
