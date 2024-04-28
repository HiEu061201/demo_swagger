package com.example.finaldemo.services.impl;

import com.example.finaldemo.exception.InvalidDataException;
import com.example.finaldemo.dto.userDTO.CreateUserDTO;
import com.example.finaldemo.dto.userDTO.UpdateUserDTO;
import com.example.finaldemo.entities.User;
import com.example.finaldemo.repository.UserRepository;
import com.example.finaldemo.services.interfaces.IExcelUploadService;
import com.example.finaldemo.services.interfaces.IUserService;
import com.example.finaldemo.utils.StringDealer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final StringDealer stringDealer = new StringDealer();
    private final PasswordEncoder passwordEncoder;
    private final IExcelUploadService iExcelUploadService;


    @Override
    public ResponseEntity<?> getAllUser(Long userId) {
        Optional<User> users = userRepository.findById(userId);
        if (users.isPresent()) {
            return new ResponseEntity<>(users.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Không tìm thấy thông tin", HttpStatus.NOT_FOUND);
    }

    @Override
    @Transactional
    public ResponseEntity<?> createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        // list all User
        List<User> userList = userRepository.findAll();

        //Trim inputs
        String email = stringDealer.trimMax(createUserDTO.getUserEmail());
        String password = stringDealer.trimMax(createUserDTO.getPassword());
        String role = stringDealer.trimMax(createUserDTO.getUserRole());

        switch (role.toLowerCase()) {
            case "admin":
                return new ResponseEntity<>("Không thể tạo tài khoản là Admin", HttpStatus.BAD_REQUEST);
            case "customer":
                break;
            default:
                return new ResponseEntity<>("Role không đúng", HttpStatus.BAD_REQUEST);
        }

        // Email is empty
        if ("".equals(email)) {
            return new ResponseEntity<>("Email không được để trống", HttpStatus.BAD_REQUEST);
        }
        // Email is not valid
        if (!stringDealer.checkEmailRegex(email)) {
            return new ResponseEntity<>("Email không đúng định dạng", HttpStatus.BAD_REQUEST);
        }
        if (email.length() > 64) {
            return new ResponseEntity<>("Email không được dài hơn 64 kí tự", HttpStatus.BAD_REQUEST);
        }

        // check duplicate email
        Optional<User> duplicateEmail = userList.stream().filter(users -> users.getEmail().equalsIgnoreCase(email)).findFirst();

        if (duplicateEmail.isPresent()) {
            return new ResponseEntity<>("Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        // Password is empty
        if ("".equals(password)) {
            return new ResponseEntity<>("Mật khẩu không được để trống", HttpStatus.BAD_REQUEST);
        }
        // Password is not valid
        if (!stringDealer.checkPasswordRegex(password)) {
            return new ResponseEntity<>("Mật khẩu không đúng định dạng", HttpStatus.BAD_REQUEST);
        }
        // Confirm password
        String rePassword = stringDealer.trimMax(createUserDTO.getRePassword());
        // Password match
        if (!password.equals(rePassword)) { /* Password not match */
            return new ResponseEntity<>("Mật khẩu không khớp", HttpStatus.BAD_REQUEST);
        }
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));

        try {
            // save user
            userRepository.save(user);
            return new ResponseEntity<>("Tạo thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi tạo người dùng", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        // find User by Id
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("Người dùng đã được xóa", HttpStatus.OK);
        }
        return new ResponseEntity<>("Không tìm thấy Id", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> updateUser(UpdateUserDTO updateUserDTO) {
        Long uId = updateUserDTO.getUserId();
        String newEmail = stringDealer.trimMax(updateUserDTO.getUserEmail());
        //  Email is empty
        if ("".equals(newEmail)) {
            return new ResponseEntity<>("Email không được để trống", HttpStatus.BAD_REQUEST);
        }
        //   Email is not valid
        if (!stringDealer.checkEmailRegex(newEmail)) {
            return new ResponseEntity<>("Email không đúng định dạng", HttpStatus.BAD_REQUEST);
        }
        // find User by email
        String oldEmail = userRepository.findUserEmailById(uId);
        // check  Email
        if (!newEmail.equals(oldEmail)) {
            if (userRepository.existsUserByEmail(newEmail)) {
                return new ResponseEntity<>("Email đã được sử dụng", HttpStatus.BAD_REQUEST);
            }
        }
        // find User by Id
        Optional<User> userOptional = userRepository.findById(updateUserDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmail(newEmail);
            // Save
            userRepository.save(user);
            return new ResponseEntity<>("Cập nhật thông tin thành công", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Có lỗi xảy ra, vui lòng thử lại sau.", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<?> getAllUserPaging(Long userId, Integer page, Integer size) {
        try {
            Page<User> userPage;
            Pageable pageable = PageRequest.of(page, size);
            if (userId == null) {
                userPage = userRepository.findAll(pageable);
            } else {
                userPage = userRepository.findUserById(userId, pageable);
            }
            return new ResponseEntity<>(userPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> saveCustomersToDatabase(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String contentType = file.getContentType();
            List<User> users;
            switch (contentType) {
                case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                    users = iExcelUploadService.getCustomersDataFromExcel(inputStream);
                    break;
                case "text/plain":
                    users = iExcelUploadService.getUserDataFromTxt(inputStream);
                    break;
                default:
                    return new ResponseEntity<>("The file is not a valid excel file", HttpStatus.BAD_REQUEST);
            }

            userRepository.saveAll(users);

            return new ResponseEntity<>("Upload successful", HttpStatus.OK);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error processing file");
        } catch (InvalidDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getListUser() {
        return userRepository.findAll();
    }


}
