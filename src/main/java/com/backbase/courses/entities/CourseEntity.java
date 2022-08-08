package com.backbase.courses.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="COURSE")
public class CourseEntity {
 
    @Id
    @GeneratedValue
    private Long id;
     
    @Column(name="title")
    private String title;
     
    @Column(name="startDate")
    private Date startDate;
     
    @Column(name="endDate")
    private Date endDate;

    @Column(name="capacity")
    private int capacity;

    @Column(name="remainingPlaces")
    private int remainingPlaces;

    @Column(name="participants")
    @ElementCollection
    private List<ParticipantEntity> participants = new ArrayList<>();

    @Override
    public String toString() {
        return "Course [" +
                "id=" + id +
                ", title=" + title +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", capacity=" + capacity +
                ", remainingPlaces=" + remainingPlaces +
                ", participants=" + participants +
                "]";
    }
}
