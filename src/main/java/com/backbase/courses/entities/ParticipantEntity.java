package com.backbase.courses.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.sql.Date;

@Data
@Getter @Setter @NoArgsConstructor
@Embeddable
public class ParticipantEntity {
    private String name;
    private Date registrationDate;
    private Date cancelDate;
}
