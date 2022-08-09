package com.backbase.courses.controllers;

import com.backbase.courses.entities.CourseEntity;
import com.backbase.courses.entities.ParticipantEntity;
import com.backbase.courses.exceptions.NotAllowedException;
import com.backbase.courses.exceptions.RecordNotFoundException;
import com.backbase.courses.services.CourseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseController.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseService courseService;

    private final Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();

    @Test
    public void given_course_when_createCourse_then_returnsCourseWithIdAndRemainingPlacesAndCreatedStatus()
            throws Exception {
        CourseEntity course1 = new CourseEntity();
        course1.setTitle("Course 1");
        int capacity = 10;
        course1.setCapacity(capacity);
        course1.setStartDate(Date.valueOf("2022-08-10"));
        course1.setEndDate(Date.valueOf("2022-08-13"));
        long courseId = 1;

        Mockito.when(courseService.createOrUpdateCourse(Mockito.any())).thenAnswer(invocation -> {
            CourseEntity givenCourse = (CourseEntity) invocation.getArguments()[0];
            givenCourse.setId(courseId);
            return givenCourse;
        });

        mvc.perform(
                MockMvcRequestBuilders.post("/courses")
                        .content(gson.toJson(course1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.remainingPlaces").value(capacity));
    }

    @Test
    public void given_courseWithTitleExists_when_getCourseByTitle_then_returnsCourseList() throws Exception {
        CourseEntity course1 = new CourseEntity();
        course1.setTitle("Course 1");
        course1.setCapacity(10);
        course1.setRemainingPlaces(10);
        course1.setStartDate(Date.valueOf("2022-08-10"));
        course1.setEndDate(Date.valueOf("2022-08-13"));
        course1.setId(1L);

        List<CourseEntity> courseList = List.of(course1);
        Mockito.when(courseService.getCoursesByTitle(Mockito.any())).thenReturn(courseList);

        mvc.perform(MockMvcRequestBuilders.get("/courses?q=title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Course 1"))
                .andExpect(jsonPath("$[0].remainingPlaces").value(10));

    }

    @Test
    public void given_courseWithTitleDoesNotExist_when_getCourseByTitle_then_returns404() throws Exception {
        Mockito.when(courseService.getCoursesByTitle(Mockito.any())).thenThrow(new RecordNotFoundException(""));

        mvc.perform(MockMvcRequestBuilders.get("/courses?q=title")).andExpect(status().isNotFound());
    }

    @Test
    public void given_serviceMethodRaisesGenericException_when_getCourseByTitle_then_returns500WithMsg() throws Exception {
        String errorMsg = "some error";
        Mockito.when(courseService.getCoursesByTitle(Mockito.any())).thenThrow(new RuntimeException(errorMsg));

        mvc.perform(MockMvcRequestBuilders.get("/courses?q=title"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(errorMsg));
    }

    @Test
    public void given_courseWithIdExists_when_getCourseById_then_returnsCourse() throws Exception {
        CourseEntity course1 = new CourseEntity();
        course1.setTitle("Course 1");
        course1.setCapacity(10);
        course1.setRemainingPlaces(10);
        course1.setStartDate(Date.valueOf("2022-08-10"));
        course1.setEndDate(Date.valueOf("2022-08-13"));
        course1.setId(1L);

        Mockito.when(courseService.getCourseById(Mockito.any())).thenReturn(course1);

        mvc.perform(MockMvcRequestBuilders.get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Course 1"))
                .andExpect(jsonPath("$.remainingPlaces").value(10));

    }

    @Test
    public void given_courseWithIdDoesNotExist_when_getCourseById_then_returns404() throws Exception {
        Mockito.when(courseService.getCourseById(Mockito.any())).thenThrow(new RecordNotFoundException(""));

        mvc.perform(MockMvcRequestBuilders.get("/courses/1")).andExpect(status().isNotFound());
    }

    @Test
    public void given_serviceMethodRaisesGenericException_when_getCourseById_then_returns500WithMsg() throws Exception {
        String errorMsg = "some error";
        Mockito.when(courseService.getCourseById(Mockito.any())).thenThrow(new RuntimeException(errorMsg));

        mvc.perform(MockMvcRequestBuilders.get("/courses/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(errorMsg));
    }

    @Test
    public void given_serviceEnrollInCourseIsSuccessful_when_enrollInCourse_then_returnsCourseWithParticipant()
            throws Exception {
        CourseEntity course1 = new CourseEntity();
        course1.setTitle("Course 1");
        course1.setCapacity(10);
        course1.setRemainingPlaces(10);
        course1.setStartDate(Date.valueOf("2022-08-10"));
        course1.setEndDate(Date.valueOf("2022-08-13"));
        course1.setId(1L);

        String participantName = "Hamid";
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName(participantName);
        participant.setRegistrationDate(Date.valueOf("2022-08-05"));
        List<ParticipantEntity> participantList = new ArrayList<>();
        participantList.add(participant);

        Mockito.when(courseService.enrollInCourse(Mockito.any(), Mockito.any())).thenAnswer(invocation -> {
            course1.setParticipants(participantList);
            return course1;
        });

        mvc.perform(
                MockMvcRequestBuilders.post("/courses/1/add")
                        .content(gson.toJson(course1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0].name").value(participantName));
    }

    @Test
    public void given_serviceEnrollRaisesRecordNotFound_when_enrollInCourse_then_returns404() throws Exception {
        String participantName = "Hamid";
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName(participantName);
        participant.setRegistrationDate(Date.valueOf("2022-08-05"));

        Mockito.when(courseService.enrollInCourse(Mockito.any(), Mockito.any()))
                .thenThrow(new RecordNotFoundException(""));

        mvc.perform(
                MockMvcRequestBuilders.post("/courses/1/add")
                        .content(gson.toJson(participant))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void given_serviceEnrollRaisesNotAllowed_when_enrollInCourse_then_returns400() throws Exception {
        String participantName = "Hamid";
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName(participantName);
        participant.setRegistrationDate(Date.valueOf("2022-08-05"));

        String errMsg = "not allowed";
        Mockito.when(courseService.enrollInCourse(Mockito.any(), Mockito.any()))
                .thenThrow(new NotAllowedException(errMsg));

        mvc.perform(
                MockMvcRequestBuilders.post("/courses/1/add")
                        .content(gson.toJson(participant))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void given_serviceEnrollRaisesOtherException_when_enrollInCourse_then_returns500() throws Exception {
        String participantName = "Hamid";
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName(participantName);
        participant.setRegistrationDate(Date.valueOf("2022-08-05"));

        String errMsg = "some error";
        Mockito.when(courseService.enrollInCourse(Mockito.any(), Mockito.any()))
                .thenThrow(new RuntimeException(errMsg));

        mvc.perform(
                MockMvcRequestBuilders.post("/courses/1/add")
                        .content(gson.toJson(participant))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(errMsg));
    }
}
