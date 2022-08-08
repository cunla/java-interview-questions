package com.backbase.courses.services;

import com.backbase.courses.entities.CourseEntity;
import com.backbase.courses.entities.ParticipantEntity;
import com.backbase.courses.exceptions.NotAllowedException;
import com.backbase.courses.exceptions.RecordNotFoundException;
import com.backbase.courses.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
     
    @Autowired
    CourseRepository repository;

    public CourseEntity createOrUpdateCourse(CourseEntity courseEntity)
    {
        return repository.save(courseEntity);
    }

    public CourseEntity getCourseById(Long id) throws RecordNotFoundException
    {
        Optional<CourseEntity> course = repository.findById(id);

        if (course.isPresent()) {
            return course.get();
        } else {
            throw new RecordNotFoundException("No course exist for given id " + id);
        }
    }

    public List<CourseEntity> getCoursesByTitle(String title) throws RecordNotFoundException {
        List<CourseEntity> courses = repository.findByTitleContains(title);

        if (courses.size() > 0) {
            return courses;
        } else {
            throw new RecordNotFoundException("No course exist for given title " + title);
        }
    }

    public CourseEntity enrollInCourse(Long courseId, ParticipantEntity participant)
            throws RecordNotFoundException, NotAllowedException {
        CourseEntity course = this.getCourseById(courseId);

        String notAllowedBaseMsg = "Enrollment is not allowed in course " + courseId + " because ";
        if (course.getRemainingPlaces() == 0) {
            throw new NotAllowedException(notAllowedBaseMsg + "the course is full");
        } else if (participant.getRegistrationDate().after(course.getStartDate())) {
            throw new NotAllowedException(
                    notAllowedBaseMsg + "the registration date is after the course start date"
            );
        } else if (course.getStartDate().getTime() - participant.getRegistrationDate().getTime() <= 3 * 86400000) {
            throw new NotAllowedException(
                    notAllowedBaseMsg + "the registration date is less than 3 days before the course start date"
            );

        } else if (course.getParticipants().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(participant.getName()))) {
            throw new NotAllowedException(notAllowedBaseMsg +
                    "participant " + participant.getName() + " is already registered in the course"
            );
        }

        course.setRemainingPlaces(course.getRemainingPlaces() - 1);
        course.getParticipants().add(participant);
        return this.createOrUpdateCourse(course);
    }
}
