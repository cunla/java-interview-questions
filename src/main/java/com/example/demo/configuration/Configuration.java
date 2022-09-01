package com.example.demo.configuration;

import com.example.demo.mapper.HolidayResponseMapper;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public HolidayResponseMapper getMapper(){
        return new HolidayResponseMapper();
    }
}
