package com.example.finaldemo.controller;

import com.example.finaldemo.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cus")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Customer")
@RequiredArgsConstructor
public class CustomerController {
    private final IUserService userService;
    @GetMapping("/search")
    public ResponseEntity<?> getUserByUserId(@RequestParam(required = false) Long userId,
                                             @RequestParam(defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return userService.getAllUserPaging(userId, page,size);
    }




}
