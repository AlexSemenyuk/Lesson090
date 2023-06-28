package org.itstep.studentservice.dto;

import jakarta.validation.constraints.*;
import org.itstep.studentservice.domain.Student;

import java.time.LocalDate;
import java.util.Date;

public record StudentDto(
        Integer id,
        @NotBlank
        @Size(min = 3, max = 50)
        String firstName,
        @NotBlank
        @Size(min = 3, max = 50)
        String lastName,
        @Past
        Date birthday,
        //  +38 095 000 00 01
        //  +3809500000 01
        //  095 000 00 00
        //  +38 095000 00 00
        @Pattern(regexp = "(\\+\\d{2})? *\\d{3} *\\d{3} *\\d{2} *\\d{2}$", message = "Wrong format of phone number")
        String phone,
        @Email(regexp = "\\w+@\\w+\\.\\w+", message = "Wrong format of email")
        String email,
        @NotNull
        AddressDto address) {
    public StudentDto(Student student, AddressDto addressDto) {
        this(student.getId(), student.getFirstName(), student.getLastName(), student.getBirthday(), student.getPhone(), student.getEmail(), addressDto);
    }
}
