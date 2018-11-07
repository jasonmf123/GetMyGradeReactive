package de.dhbw.se.getMyGradesReactive;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@SpringBootApplication
@RestController
public class GetMyGradesReactiveApplication {

    @Bean
    RouterFunction<ServerResponse> route(GradeRepo gradeRepo)
    {
        return RouterFunctions.route(
                GET("/grades/all"),
                serverRequest -> ok().body(
                        gradeRepo.findAll(), Grade.class
                ))
                .andRoute(GET("/infinite/stream"),
                serverRequest -> ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(
                        Flux.fromStream(Stream.generate( () ->
                                new Grade(null, new Random().nextInt(10000), 1.0, "Software Engineering")
                                )).delayElements(Duration.ofSeconds(1L)), Grade.class));
    }


	public static void main(String[] args) {
		SpringApplication.run(GetMyGradesReactiveApplication.class, args);
	}

	@Component
    class Initializer implements ApplicationRunner
    {
        private Integer[] matrikelNos = {1494356, 2272653, 2773463, 3023449, 3368245, 3642394, 4051479, 4409176, 4508858, 4555825, 5177671, 5697216, 5851598, 5988150, 6588772, 6648811, 6739256, 6801150, 6887900, 7019687, 7080779, 7264776, 7268520, 7322144, 7566015, 7719749, 7785046, 8598230, 9633102, 9883275, 1494356, 2272653, 2773463, 3023449, 3368245, 3642394, 4051479, 4409176, 4508858, 4555825, 5177671, 5697216, 5851598, 5988150, 6588772, 6648811, 6739256, 6801150, 6887900, 7019687, 7080779, 7264776, 7268520, 7322144, 7566015, 7719749, 7785046, 8598230, 9633102, 9883275,};
        private String[] subjects = {"Software Engineering", "Webengineering", "Formale Sprachen", "Geschäftsprozesse", "Statistik", "Rechnerarchitektur", "Netzwerktechnik", "Datenbanken", "Betriebssysteme" };
        private Random rnd = new Random();
        private final GradeRepo gradeRepo;

        public Initializer(GradeRepo gradeRepo) {
            this.gradeRepo = gradeRepo;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            gradeRepo.deleteAll().thenMany(
            Flux.fromArray(matrikelNos)
                    .map(nr -> new Grade(null, nr,
                            rnd.nextInt(10)/10.0+1,
                            subjects[rnd.nextInt(subjects.length)] ))
                    .flatMap(grade -> gradeRepo.save(grade)))
            .thenMany(
            gradeRepo.findAll()).subscribe(System.out::println);

        }
    }


}
