package org.itstep.studentservice.service;

import feign.Response;
import lombok.RequiredArgsConstructor;
import org.itstep.studentservice.dto.AddressDto;
import org.itstep.studentservice.client.AddressClient;
import org.itstep.studentservice.command.CreateAddressCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressClient client;

//    public List<AddressDto> findAll() {
//        List<AddressDto> addressDtoList = client.findAll();
//        return addressDtoList;
//    }
    public AddressDto findById(Integer id) {
        AddressDto addressDto = client.findById(id);
        return addressDto;
    }
    public AddressDto save(CreateAddressCommand command) {
        Response response = client.save(command);
        AddressDto addressDto = null;
        if (response.status() == HttpStatus.CREATED.value()) {
            Collection<String> locations = response.headers().get("location");
            String location = locations.iterator().next();
            Path path = Path.of(location);
            String id = path.getName(path.getNameCount() - 1).toString();
            addressDto = findById(Integer.valueOf(id));
        }
        return addressDto;
    }
    public AddressDto update(Integer id,CreateAddressCommand command) {
        Response response = client.update(id, command);
        AddressDto addressDto = null;
        if (response.status() == 200) {
//            Collection<String> locations = response.headers().get("location");
//            String location = locations.iterator().next();
//            Path path = Path.of(location);
//            String idTmp = path.getName(path.getNameCount() - 1).toString();
            addressDto = client.findById(id);
        }
        return addressDto;
    }

    public ResponseEntity<?> delete(Integer addressId) {
        return client.delete(addressId);
    }
}
