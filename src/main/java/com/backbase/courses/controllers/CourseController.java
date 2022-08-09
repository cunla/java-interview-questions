package com.backbase.courses.controllers;

import com.backbase.courses.entities.CourseEntity;
import com.backbase.courses.entities.ParticipantEntity;
import com.backbase.courses.exceptions.NotAllowedException;
import com.backbase.courses.services.CourseService;
import com.backbase.courses.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


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
        return new ResponseEntity<>(courseWithId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getCourseByTitle(@RequestParam(value = "q") String title) {
        try {
            return new ResponseEntity<>(service.getCoursesByTitle(title), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(service.getCourseById(id), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{courseId}/add")
    public ResponseEntity<?> enrollInCourse(@PathVariable Long courseId, @RequestBody ParticipantEntity participant) {
        try {
            return new ResponseEntity<>(service.enrollInCourse(courseId, participant), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
