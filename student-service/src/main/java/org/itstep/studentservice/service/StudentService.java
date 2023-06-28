package org.itstep.studentservice.service;

import feign.Response;
import lombok.RequiredArgsConstructor;
import org.itstep.studentservice.dto.AddressDto;
import org.itstep.studentservice.command.CreateStudentCommand;
import org.itstep.studentservice.domain.Student;
import org.itstep.studentservice.dto.StudentDto;
import org.itstep.studentservice.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final AddressService addressService;

    public List<StudentDto> findAll() {
        List<StudentDto> studentDtoList = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        if (students.size() > 0) {
            for (Student st : students) {
                AddressDto addressDto = addressService.findById(st.getAddressId());
                StudentDto studentDtoTmp = new StudentDto(st, addressDto);
                if (studentDtoTmp != null) {
                    studentDtoList.add(studentDtoTmp);
                }
            }
        }
        return studentDtoList;
    }

    public Optional<Student>  findById(Integer id) {
        return studentRepository.findById(id);
    }

    public StudentDto findFullStudentById(Integer id) {
        Optional<Student> optionalStudent = findById(id);
        StudentDto studentDtoTmp = null;
        if (optionalStudent.isPresent()) {
            Student studentTmp = optionalStudent.get();
            AddressDto addressDto = addressService.findById(studentTmp.getAddressId());
            studentDtoTmp = new StudentDto(studentTmp, addressDto);
        }
        return studentDtoTmp;
    }

    public StudentDto save(CreateStudentCommand command) {
        // save address
        AddressDto addressDto = addressService.save(command.toCreateAddressCommand());
        Student studentEntity = command.toStudentEntity();
        studentEntity.setAddressId(addressDto.id());
        Student student = studentRepository.save(studentEntity);
        return new StudentDto(student, addressDto);
    }

    public StudentDto update(Integer id, CreateStudentCommand command) {
        Optional<Student> optionalStudent = findById(id);
        Student studentTmp = null;
        AddressDto addressDto = null;
        StudentDto studentDto = null;
        if (optionalStudent.isPresent()) {
            studentTmp = optionalStudent.get();
            studentTmp.setFirstName(command.firstName());
            studentTmp.setLastName(command.lastName());
            studentTmp.setBirthday(command.birthday());
            studentTmp.setPhone(command.phone());
            studentTmp.setEmail(command.email());
            addressDto = addressService.update(studentTmp.getAddressId(), command.toCreateAddressCommand());
            Student student = studentRepository.save(studentTmp);
            studentDto = new StudentDto(student, addressDto);
        }
        return studentDto;
    }

    public ResponseEntity<?> delete(Integer id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        AddressDto addressDto = null;
        ResponseEntity responseAddress = null;
        ResponseEntity responseStudent = null;
        if (optionalStudent.isPresent()) {
            Student studentTmp = optionalStudent.get();
            responseAddress = addressService.delete(studentTmp.getAddressId());
            if (responseAddress.getStatusCode().value() == 204){
                studentRepository.delete(studentTmp);
            }

            return ResponseEntity.noContent().build();
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Student by id=%s not found".formatted(id));
        problemDetail.setTitle("Error delete student");
        return ResponseEntity.badRequest()
                .body(problemDetail);
    }
}
