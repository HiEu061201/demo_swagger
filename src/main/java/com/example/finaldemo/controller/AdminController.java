package com.example.finaldemo.controller;

import com.example.finaldemo.dto.userDTO.UpdateUserDTO;
import com.example.finaldemo.dto.userDTO.CreateUserDTO;
import com.example.finaldemo.services.interfaces.IExcelExportService;
import com.example.finaldemo.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin")
@RequiredArgsConstructor
public class AdminController {
    private final IUserService userService;
    private final IExcelExportService getDataDownloaded;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUserByUserId(@RequestParam(required = false) Long userId,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return userService.getAllUserPaging(userId, page, size);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        return userService.updateUser(updateUserDTO);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadUserData(@RequestParam("file") MultipartFile file) {
        return userService.saveCustomersToDatabase(file);
    }

    @GetMapping("/download")
    private ResponseEntity<?> download(@RequestParam(required = false) Long userId,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam String fileType) throws IOException {
        return getDataDownloaded.getDataDownloaded(userId, page, size,fileType);
    }


}
