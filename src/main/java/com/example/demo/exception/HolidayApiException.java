package com.example.demo.exception;

import com.example.demo.entity.HolidayError;
import lombok.Getter;

@Getter
public class HolidayApiException extends Exception {
    private HolidayError error;

    public HolidayApiException(HolidayError error) {
        this.error = error;
    }

}
