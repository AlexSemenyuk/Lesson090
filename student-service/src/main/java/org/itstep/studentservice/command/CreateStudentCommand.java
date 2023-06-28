package org.itstep.studentservice.command;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;
import org.itstep.studentservice.domain.Student;

import java.util.Date;

public record CreateStudentCommand(
        @Size(min = 3, max = 50)
        @NotBlank
        String firstName,

        @NotBlank
        @Size(min = 3, max = 50)
        String lastName,

        @Past
        Date birthday,
        @Pattern(regexp = "(\\+\\d{2})? *\\d{3} *\\d{3} *\\d{2} *\\d{2}$", message = "Wrong format of phone number")
        String phone,

        @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Wrong format of email")
        String email,

        @NotBlank
        @Size(min = 1, max = 50)
        String country,

        @NotBlank
        @Size(min = 1, max = 50)
        String city,

        @NotBlank
        @Size(min = 1, max = 100)
        String addressLine1,

        String addressLine2
) {

        public Student toStudentEntity() {
                return Student.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .birthday(birthday)
                        .email(email)
                        .phone(phone)
                        .build();
        }

        public CreateAddressCommand toCreateAddressCommand() {
                return new CreateAddressCommand(country, city, addressLine1, addressLine2);
        }
}
