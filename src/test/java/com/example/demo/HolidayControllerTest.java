package com.example.demo;

import com.example.demo.api.HolidayController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HolidayController.class)
class HolidayControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
        mvc.perform(get("/holiday").param("country", "US"))
                .andExpect(status().isBadRequest());
    }
}
