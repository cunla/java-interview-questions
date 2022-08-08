package com.backbase.courses;

import com.backbase.courses.services.CourseService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CourseServiceTestConfiguration {

    @Bean
    public CourseService courseService() {
        return new CourseService();
    }
}
