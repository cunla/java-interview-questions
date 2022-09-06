package com.example.demo.integration;

import com.example.demo.DemoApplication;
import com.example.demo.dto.NearestHoliday;
import com.example.demo.entity.HolidayError;
import com.example.demo.entity.Response;
import com.example.demo.exception.HolidayApiException;
import com.example.demo.service.HolidayService;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("integration")
@SpringBootTest(classes = DemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HolidayControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private TestRestTemplate template;

    @RegisterExtension
    static WireMockExtension server = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8080))
            .build();

    @Test
    @DisplayName("Should controller get valid nearest holiday info.")
    void when_InputIsValid_thenReturnValidResponse() {
        NearestHoliday holiday = this.template.getForObject("http://localhost:" + port + "/holiday?date=2021-10-15&country=US", NearestHoliday.class);
        assertEquals("2021-12-31", holiday.getNearestHolidayDate());
        assertEquals("New Year's Eve", holiday.getHolidayName());
    }

    @Test
    @DisplayName("Should controller get error message when country is not supported.")
    void when_CountryIsWrong_thenReturnErrorResponse() {
        HolidayError exception = this.template.getForObject("http://localhost:" + port + "/holiday?date=2021-10-15&country=UK", HolidayError.class);
        assertEquals("The requested country is not supported", exception.getError());
        assertEquals(400, exception.getStatus());
    }

    @Test
    @DisplayName("Should get valid nearest holiday info.")
    void when_DateAndCountry_thenReturnValidResponse() {
        String country = "US";
        String year = "2021";
        String month = "10";
        String day = "15";
        Response response = holidayService.getHoliday(
                country, year, month, day
        );

        assertEquals(response.getHolidays().get(0).getName(), "New Year's Eve");
        assertEquals(response.getHolidays().get(0).getDate(), "2021-12-31");
        assertEquals(response.getStatus(), 200);
    }

    @Test
    @DisplayName("Should not get valid holiday info if no date is wrong formatted.")
    void when_DateIsWrong_thenReturnInValidResponse() {
        server.listAllStubMappings().getMappings().forEach(System.out::println);
        String country = "US";
        String year = "2021";
        String month = "111";
        String day = "222";

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            holidayService.getHoliday(country, year, month, day);
        });

        String errorMessage = "The requested date (2021-112-01) is invalid. For more information, please visit https://holidayapi.com/docs";
        assertEquals(((HolidayApiException) exception.getCause()).getError().getError(), errorMessage);
    }

    @Test
    @DisplayName("Should not get valid holiday info if country is not supported.")
    void when_CountryIsWrong_thenReturnInValidResponse() {

        String country = "UK";
        String year = "2021";
        String month = "10";
        String day = "15";
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            holidayService.getHoliday(country, year, month, day);
        });

        String errorMessage = "The requested country is not supported";
        assertEquals(((HolidayApiException) exception.getCause()).getError().getError(), errorMessage);
    }
}
