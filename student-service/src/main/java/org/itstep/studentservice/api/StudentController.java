package org.itstep.studentservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itstep.studentservice.command.CreateStudentCommand;
import org.itstep.studentservice.domain.Student;
import org.itstep.studentservice.dto.StudentDto;
import org.itstep.studentservice.repository.StudentRepository;
import org.itstep.studentservice.service.StudentService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;


@RequestMapping("/api/v1/student/")
@RequiredArgsConstructor
@RestController
@Slf4j
@CrossOrigin(origins = "${ui.host}", methods = {
        RequestMethod.GET,
        RequestMethod.DELETE,
        RequestMethod.PUT,
        RequestMethod.POST
})
public class StudentController {
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    // findAll students
    @GetMapping
    public ResponseEntity<List<StudentDto>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    // Details for student
    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id) {
        StudentDto studentDto = studentService.findFullStudentById(id);
        if (studentDto != null){
            return new ResponseEntity<>(studentDto, HttpStatus.OK);
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Student by id=%s not found".formatted(id));
        problemDetail.setTitle("Error find student");
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }

    // create student
    @PostMapping(consumes = {
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> save(@Validated @RequestBody CreateStudentCommand command, BindingResult bindingResult) {
        String checkStudent = checkStudent(command);
        ResponseEntity<?> response = null;
            if (checkStudent.equals("")) {
                try {
                    if (!bindingResult.hasErrors()) {
                        StudentDto student = studentService.save(command);
                        response = ResponseEntity
                                .created(URI.create("/api/v1/student/%d".formatted(student.id())))
                                .build();
                    } 
                } catch (Exception exception) {
                    log.error(exception.getMessage(), exception);
                    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    problemDetail.setTitle("Can't create student");
                    problemDetail.setDetail(exception.getMessage());
                    problemDetail.setInstance(URI.create("/api/v1/student"));
                    response = ResponseEntity
                            .of(problemDetail)
                            .build();
                }
            } else {
                ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                problemDetail.setDetail(checkStudent);
//                    problemDetail.setDetail(
//                        bindingResult.getFieldErrors()
//                                .stream()
//                                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
//                                .collect(Collectors.joining(", "))
//                );
                problemDetail.setTitle("Can't create student");
                problemDetail.setInstance(URI.create("/api/v1/student"));
                response = ResponseEntity
                        .of(problemDetail)
                        .build();
            }
        return response;
    }


    // update students
    @PutMapping("{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                      @RequestBody @Validated CreateStudentCommand command,
                                     BindingResult bindingResult) {

        String checkStudent = checkStudent(command);
        ResponseEntity<?> response = null;
        if (checkStudent.equals("")) {
            try {
                if (!bindingResult.hasErrors()) {
                    StudentDto student = studentService.update(id, command);
                    response = new ResponseEntity<>(student, HttpStatus.OK);
                }
            } catch (Exception exception) {
                log.error(exception.getMessage(), exception);
                ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                problemDetail.setTitle("Can't update student");
                problemDetail.setDetail(exception.getMessage());
                problemDetail.setInstance(URI.create("/api/v1/student"));
                response = ResponseEntity
                        .of(problemDetail)
                        .build();
            }
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setDetail(checkStudent);
            problemDetail.setTitle("Can't update student");
            problemDetail.setInstance(URI.create("/api/v1/student"));
            response = ResponseEntity
                    .of(problemDetail)
                    .build();
        }
        return response;
    }

//    @PutMapping("{id}")
//    public ResponseEntity<?> update(@PathVariable Integer id,
//                                    @RequestBody @Validated Student newStudent,
//                                    BindingResult bindingResult) {
//        ResponseEntity<?> response;
//        try {
//            if(!bindingResult.hasErrors()) {
//                Student updatedStudent = studentRepository.findById(id).orElseThrow();
//                updatedStudent.setFirstName(newStudent.getFirstName());
//                updatedStudent.setLastName(newStudent.getLastName());
//                updatedStudent.setBirthday(newStudent.getBirthday());
//                updatedStudent.setEmail(newStudent.getEmail());
//                updatedStudent.setPhone(newStudent.getPhone());
//                response = ResponseEntity.ok(studentRepository.save(updatedStudent));
//            } else {
//                var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
//                problemDetail.setTitle("Can't update student");
//                problemDetail.setDetail(
//                        bindingResult.getFieldErrors()
//                                .stream()
//                                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
//                                .collect(Collectors.joining(", "))
//                );
//                problemDetail.setInstance(URI.create("/api/v1/student"));
//                response = ResponseEntity
//                        .of(problemDetail)
//                        .build();
//            }
//        } catch (NoSuchElementException ex) {
//            log.warn("Student by id {} have not found", id);
//            var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
//            problemDetail.setTitle("Can't update student");
//            problemDetail.setDetail(ex.getMessage());
//            problemDetail.setInstance(URI.create("/api/v1/student/%d".formatted(id)));
//            response = ResponseEntity
//                    .of(problemDetail)
//                    .build();
//        }catch (IllegalArgumentException | OptimisticLockingFailureException ex) {
//            log.error(ex.getMessage(), ex);
//            var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            problemDetail.setTitle("Can't update student");
//            problemDetail.setDetail(ex.getMessage());
//            problemDetail.setInstance(URI.create("/api/v1/student/%d".formatted(id)));
//            response = ResponseEntity
//                    .of(problemDetail)
//                    .build();
//        }
//        return response;
//    }




    // delete student
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        ResponseEntity<?> response = null;
        try {
                response = studentService.delete(id);
//                response = new ResponseEntity<>(student, HttpStatus.OK);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            problemDetail.setTitle("Can't delete student");
            problemDetail.setDetail(exception.getMessage());
            problemDetail.setInstance(URI.create("/api/v1/student"));
            response = ResponseEntity
                    .of(problemDetail)
                    .build();
        }
       return response;
    }
//    @DeleteMapping("{id}")
//    public ResponseEntity<?> delete(@PathVariable Integer id) {
//        ResponseEntity<?> response;
//        try {
//            Student student = studentRepository.findById(id).orElseThrow();
//            studentRepository.delete(student);
//            response = ResponseEntity.noContent().build();
//        } catch (NoSuchElementException ex) {
//            log.warn("Student by id {} have not found", id);
//            var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
//            problemDetail.setTitle("Can't delete student");
//            problemDetail.setDetail(ex.getMessage());
//            problemDetail.setInstance(URI.create("/api/v1/student/%d".formatted(id)));
//            response = ResponseEntity
//                    .of(problemDetail)
//                    .build();
//        } catch (IllegalArgumentException | OptimisticLockingFailureException ex) {
//            log.error(ex.getMessage(), ex);
//            var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            problemDetail.setTitle("Can't delete student");
//            problemDetail.setDetail(ex.getMessage());
//            problemDetail.setInstance(URI.create("/api/v1/student/%d".formatted(id)));
//            response = ResponseEntity
//                    .of(problemDetail)
//                    .build();
//        }
//        return response;
//    }





    private String checkStudent(CreateStudentCommand command) {
        // 1. Student
        // 1.1 firstName and lastName
        String message = "";
        if (command.firstName().length() < 3 || command.firstName().length() > 50) {
            message += "Students first name don't situated between 3..50, ";
        }
        if (command.lastName().length() < 3 || command.lastName().length() > 50) {
            message += "Students last name don't situated between 3..50, ";
        }
        // 1.2 birthday
        Date currentDate = new Date();
        System.out.println("Текущая дата и время: " + currentDate);
        // Сравнение двух дат
        int comparisonResult = command.birthday().compareTo(currentDate);
//        if (comparisonResult < 0) {
//            System.out.println("date1 меньше, чем date2");
////            message += "Student has a wrong birthday, ";
//        } else
        if (comparisonResult >= 0) {
            System.out.println("date1 больше, чем date2");
            message += "Student has a wrong birthday, ";
        }
        // 1.3 Number
        String patternPhone = "(\\+\\d{2})? *\\d{3} *\\d{3} *\\d{2} *\\d{2}$";
        boolean isMatchOfPhone = Pattern.matches(patternPhone, command.phone());
        if (!isMatchOfPhone) {
            message += "Student has a wrong phone, ";
        }
        // 1.4 Email
//        String patternEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String patternEmail = "\\w+@\\w+\\.\\w+";
        boolean isMatchOfEmail = Pattern.matches(patternEmail, command.email());
        System.out.println("isMatchOfEmail = " + isMatchOfEmail);
        if (!isMatchOfEmail) {
            message += "Student has a wrong email";
        }
        System.out.println("message = " + message);

        // 2. Address
        // 2.1 country
        if (command.country().length() < 1 || command.country().length() > 50) {
            message += "Addresses country don't situated between 1..50, ";
        }
        // 2.2 city
        if (command.city().length() < 1 || command.city().length() > 50) {
            message += "Addresses city don't situated between 1..50, ";
        }
        // 2.3 addressLine1
        if (command.addressLine1().length() < 1 || command.addressLine1().length() > 50) {
            message += "Addresses addressLine1 don't situated between 1..50, ";
        }
        return message;
    }
}
