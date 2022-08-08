package com.backbase.courses.controllers;

import com.backbase.courses.entities.CourseEntity;
import com.backbase.courses.entities.ParticipantEntity;
import com.backbase.courses.exceptions.NotAllowedException;
import com.backbase.courses.services.CourseService;
import com.backbase.courses.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;


@RestController
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LogManager.getLogger(CourseController.class.getName());

    @Autowired
    CourseService service;

    @PostMapping
    public ResponseEntity<CourseEntity> createCourse(@RequestBody CourseEntity course) {
        logger.info("course to add is " + course);
        course.setRemainingPlaces(course.getCapacity());
        CourseEntity courseWithId = service.createOrUpdateCourse(course);
        return new ResponseEntity<CourseEntity>(courseWithId, new HttpHeaders(), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getCourseByTitle(@RequestParam(value = "q") String title) {
        try {
            List<CourseEntity> courses = service.getCoursesByTitle(title);
            return new ResponseEntity<List<CourseEntity>>(courses, new HttpHeaders(), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            CourseEntity course = service.getCourseById(id);
            return new ResponseEntity<CourseEntity>(course, new HttpHeaders(), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{courseId}/add")
    public ResponseEntity<?> enrollInCourse(@PathVariable Long courseId,
                                            @RequestBody ParticipantEntity participant) {
        try {
            CourseEntity course = service.enrollInCourse(courseId, participant);
            return new ResponseEntity<CourseEntity>(course, new HttpHeaders(), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @DeleteMapping("/{id}")
//    public HttpStatus deleteEmployeeById(@PathVariable("id") Long id)
//            throws RecordNotFoundException {
//        service.deleteEmployeeById(id);
//        return HttpStatus.FORBIDDEN;
//    }

}
