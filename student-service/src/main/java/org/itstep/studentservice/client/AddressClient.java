package org.itstep.studentservice.client;

import feign.Response;
import org.itstep.studentservice.dto.AddressDto;
import org.itstep.studentservice.command.CreateAddressCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "address-service",
        url = "http://localhost:8080",
        path = "/api/v1/address")
public interface AddressClient {
//    @GetMapping("/")
//    List<AddressDto> findAll();

    @GetMapping("/{id}")
    AddressDto findById(@PathVariable Integer id);

    @PostMapping("/")
    Response save(CreateAddressCommand address);

    @PutMapping("/{id}")
    Response update(@PathVariable Integer id, CreateAddressCommand address);

    @DeleteMapping("/{id}")
    ResponseEntity delete(@PathVariable Integer id);

}
