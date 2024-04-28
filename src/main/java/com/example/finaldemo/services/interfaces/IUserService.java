package com.example.finaldemo.services.interfaces;

import com.example.finaldemo.dto.userDTO.CreateUserDTO;
import com.example.finaldemo.dto.userDTO.UpdateUserDTO;
import com.example.finaldemo.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    ResponseEntity<?> getAllUser(Long userId);

    ResponseEntity<?> createUser(CreateUserDTO createUserDTO);

    ResponseEntity<?> deleteUser(Long id);

    ResponseEntity<?> updateUser(UpdateUserDTO updateUserDTO);

    ResponseEntity<?> getAllUserPaging(Long userId, Integer page, Integer size);

    ResponseEntity<?> saveCustomersToDatabase(MultipartFile file);

    List<User> getListUser();
}
