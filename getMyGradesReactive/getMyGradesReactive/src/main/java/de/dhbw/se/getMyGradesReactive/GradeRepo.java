package de.dhbw.se.getMyGradesReactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GradeRepo extends ReactiveMongoRepository<Grade, String> {


    Mono<Grade> getGradeById(String id);

    Flux<Grade> getGradesByMatrikelNr(Integer matrikelnr);

    Flux<Grade> getGradesBySubject(String subject);
}
