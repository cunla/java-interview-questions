package com.backbase.courses.services;
import com.backbase.courses.entities.CourseEntity;
import com.backbase.courses.entities.ParticipantEntity;
import com.backbase.courses.exceptions.NotAllowedException;
import com.backbase.courses.exceptions.RecordNotFoundException;
import com.backbase.courses.repositories.CourseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@Import(CourseServiceTestConfiguration.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @MockBean
    private CourseRepository courseRepository;

    private CourseEntity availableCourse = new CourseEntity();
    private CourseEntity fullCourse = new CourseEntity();
    private ParticipantEntity participant = new ParticipantEntity();

    @Before
    public void setup() {
        availableCourse.setTitle("Course 1");
        availableCourse.setCapacity(10);
        availableCourse.setRemainingPlaces(10);
        availableCourse.setStartDate(Date.valueOf("2022-08-10"));
        availableCourse.setEndDate(Date.valueOf("2022-08-13"));
        availableCourse.setId(1L);

        participant.setName("Hamid");
        participant.setRegistrationDate(Date.valueOf("2022-08-05"));
        List<ParticipantEntity> participantList = new ArrayList<>();
        participantList.add(participant);
        availableCourse.setParticipants(participantList);

        fullCourse.setTitle("Full");
        fullCourse.setCapacity(20);
        fullCourse.setRemainingPlaces(0);
        fullCourse.setStartDate(Date.valueOf("2022-08-10"));
        fullCourse.setEndDate(Date.valueOf("2022-08-13"));
        fullCourse.setId(2L);


        Mockito.when(courseRepository.findById(1L)).thenReturn(Optional.of(availableCourse));
        Mockito.when(courseRepository.findByTitleContains("Course")).thenReturn(List.of(availableCourse));
        Mockito.when(courseRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void given_courseEntity_when_createOrUpdateCourse_then_repositorySaveMethodCalled() {
        CourseEntity course = new CourseEntity();
        courseService.createOrUpdateCourse(course);

        Mockito.verify(courseRepository, Mockito.times(1)).save(course);
    }

    @Test
    public void given_courseWithIdExists_when_getCourseById_then_returnCourseWithProperAttributes() {
        try {
            CourseEntity foundCourse = courseService.getCourseById(1L);
            Assert.assertEquals("Course 1", foundCourse.getTitle());
        } catch (RecordNotFoundException e) {
            Assert.fail("Course with id 1 exists in test repository");
        }
    }

    @Test
    public void given_courseWithIdDoesNotExist_when_getCourseById_then_recordNotFoundExceptionRaised() {
        long courseId = 3L;
        try {
            courseService.getCourseById(courseId);
            Assert.fail("Exception expected; Course with id " + courseId + " does not exist in test repository");
        } catch (RecordNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains(Long.toString(courseId)));
        }
    }

    @Test
    public void given_courseWithTitleExists_when_getCoursesByTitle_then_returnedListContainsOneCourse() {
        try {
            List<CourseEntity> foundCourses = courseService.getCoursesByTitle("Course");
            Assert.assertEquals(1, foundCourses.size());
        } catch (RecordNotFoundException e) {
            Assert.fail("Course with title Course exists in test repository");
        }
    }

    @Test
    public void given_courseWithTitleDoesNotExist_when_getCoursesByTitle_then_recordNotFoundExceptionRaised() {
        String title = "some title";
        try {
            List<CourseEntity> foundCourses = courseService.getCoursesByTitle(title);
            Assert.fail("Exception expected; Courses with title " + title + " does not exist in test repository");
        } catch (RecordNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains(title));
        }
    }

    @Test
    public void given_courseWithIdDoesNotExist_when_enrollInCourse_then_recordNotFoundExceptionRaised() {
        long courseId = 3L;
        try {
            courseService.enrollInCourse(courseId, new ParticipantEntity());
            Assert.fail("Exception expected; Course with id " + courseId + " does not exist in test repository");
        } catch (RecordNotFoundException e) {
            Assert.assertTrue(e.getMessage().contains(Long.toString(courseId)));
        } catch (NotAllowedException e) {
            Assert.fail("Unwanted exception raised");
        }
    }

    @Test
    public void given_registrationDateIsAfterCourseStartDate_when_enrollInCourse_then_notAllowedExceptionRaisedWithProperMessage() {
        long courseId = 1L;
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName("Hamid");
        participant.setRegistrationDate(Date.valueOf("2022-08-11"));

        try {
            courseService.enrollInCourse(courseId, participant);
            Assert.fail("Exception expected");
        } catch (RecordNotFoundException e) {
            Assert.fail("Unwanted exception raised");
        } catch (NotAllowedException e) {
            Assert.assertTrue(e.getMessage().contains("the registration date is after the course start date"));
        }
    }

    @Test
    public void given_registrationDateEqualsOrIsLessThanThreeDaysBeforeCourseStartDate_when_enrollInCourse_then_notAllowedExceptionRaisedWithProperMessage() {
        long courseId = 1L;
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName("Hamid");
        participant.setRegistrationDate(Date.valueOf("2022-08-07"));

        try {
            courseService.enrollInCourse(courseId, participant);
            Assert.fail("Exception expected");
        } catch (RecordNotFoundException e) {
            Assert.fail("Unwanted exception raised");
        } catch (NotAllowedException e) {
            Assert.assertTrue(e.getMessage().contains("the registration date is less than 3 days before the course start date"));
        }
    }

    @Test
    public void given_courseIsFull_when_enrollInCourse_then_notAllowedExceptionRaisedWithProperMessage() {
        long courseId = 2L;
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName("Hamid");
        participant.setRegistrationDate(Date.valueOf("2022-08-02"));

        Mockito.when(courseRepository.findById(2L)).thenReturn(Optional.of(fullCourse));

        try {
            courseService.enrollInCourse(courseId, participant);
            Assert.fail("Exception expected");
        } catch (RecordNotFoundException e) {
            Assert.fail("Unwanted exception raised");
        } catch (NotAllowedException e) {
            Assert.assertTrue(e.getMessage().contains("the course is full"));
        }
    }

    @Test
    public void given_participantAlreadyRegistered_when_enrollInCourse_then_notAllowedExceptionRaisedWithProperMessage() {
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName("Hamid");
        participant.setRegistrationDate(Date.valueOf("2022-08-02"));

        Mockito.when(courseRepository.findById(Mockito.any())).thenReturn(Optional.of(availableCourse));

        try {
            courseService.enrollInCourse(2L, participant);
            Assert.fail("Exception expected");
        } catch (RecordNotFoundException e) {
            Assert.fail("Unwanted exception raised");
        } catch (NotAllowedException e) {
            Assert.assertTrue(e.getMessage().contains("is already registered in the course"));
        }
    }

    @Test
    public void given_registrationAllowed_when_enrollInCourse_then_courseRemainingPlacesDecreasesAndParticipantAdded() {
        long courseId = 1L;
        ParticipantEntity participant = new ParticipantEntity();
        participant.setName("Hamid2");
        participant.setRegistrationDate(Date.valueOf("2022-08-02"));

        try {
            CourseEntity updatedCourse = courseService.enrollInCourse(courseId, participant);
            Assert.assertEquals(9, updatedCourse.getRemainingPlaces());
            Assert.assertTrue(updatedCourse.getParticipants().contains(participant));
        } catch (RecordNotFoundException|NotAllowedException e) {
            Assert.fail("Unwanted exception raised");
        }
    }
}
