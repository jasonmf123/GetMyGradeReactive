package de.dhbw.se.getMyGradesReactive;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Grade {

    @Id
    String id;
    Integer matrikelNr;
    Double grade;
    String subject;

    public Grade(String id, Integer matrikelNr, Double grade, String subject) {
        this.id = id;
        this.matrikelNr = matrikelNr;
        this.grade = grade;
        this.subject = subject;
    }
}
