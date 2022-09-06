package com.example.demo.unit;

import com.example.demo.api.HolidayController;
import com.example.demo.entity.Response;
import com.example.demo.mapper.HolidayResponseMapper;
import com.example.demo.service.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.demo.fixtures.ResponseFixtures.generate;
import static com.example.demo.fixtures.ResponseFixtures.generateNearest;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HolidayController.class)
class HolidayControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private HolidayResponseMapper mapper;

    @BeforeEach
    void setUp() {
        final Response response = generate();
        when(holidayService.getHoliday(eq("US"), anyString(), anyString(), anyString())).thenReturn(response);
        when(mapper.convert(response)).thenReturn(generateNearest());
    }

    @Test
    @DisplayName("Should not pass the validation, always return bad request.")
    void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
        mvc.perform(get("/holiday"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"filed\":\"country\",\"rejectedValue\":\"String\",\"message\":\"parameter is missing. Country must be entered !\"}"));

        mvc.perform(get("/holiday").param("date", "abcdefg"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"filed\":\"country\",\"rejectedValue\":\"String\",\"message\":\"parameter is missing. Country must be entered !\"}"));

        mvc.perform(get("/holiday").param("date", "2021-12-13"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"filed\":\"country\",\"rejectedValue\":\"String\",\"message\":\"parameter is missing. Country must be entered !\"}"));

        mvc.perform(get("/holiday").param("country", "US").param("date", "abcdeft"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"filed\":\"date\",\"rejectedValue\":\"abcdeft\",\"message\":\"parameter is invalid. Date must be yyyy-MM-dd format !\"}"));

        mvc.perform(get("/holiday").param("country", "US").param("date", "2021-2-13"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"filed\":\"date\",\"rejectedValue\":\"2021-2-13\",\"message\":\"parameter is invalid. Date must be yyyy-MM-dd format !\"}"));
    }

    @Test
    @DisplayName("Should get nearest holiday.")
    void whenInputIsValid_thenReturnsStatus200() throws Exception {
        mvc.perform(get("/holiday").param("country", "US"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"nearestHolidayDate\":\"2021-07-04\",\"holidayName\":\"National\"}"));

        mvc.perform(get("/holiday").param("country", "US").param("date", "2021-03-03"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string("{\"nearestHolidayDate\":\"2021-07-04\",\"holidayName\":\"National\"}"));
    }


}
