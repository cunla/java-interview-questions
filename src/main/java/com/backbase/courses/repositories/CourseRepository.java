package com.backbase.courses.repositories;

import com.backbase.courses.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    List<CourseEntity> findByTitleContains(String title);

}
