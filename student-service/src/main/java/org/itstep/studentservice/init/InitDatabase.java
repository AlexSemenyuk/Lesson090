package org.itstep.studentservice.init;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.itstep.studentservice.domain.Student;
import org.itstep.studentservice.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

//@Component
@RequiredArgsConstructor
public class InitDatabase implements CommandLineRunner {

    @Value("${students.count:20}")
    private Integer studentsCount;

    private final StudentRepository repository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.UK);
//        for (int i = 0; i < studentsCount; i++) {
//            repository.save(Student.builder()
//                            .firstName(faker.name().firstName())
//                            .lastName(faker.name().lastName())
//                            .birthday( LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()))
//                            .email(faker.internet().emailAddress())
//                            .phone(faker.phoneNumber().phoneNumber())
//                    .build());
//        }

    }
}
