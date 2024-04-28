package com.example.finaldemo.services.impl;


import com.example.finaldemo.entities.User;
import com.example.finaldemo.repository.UserRepository;
import com.example.finaldemo.services.interfaces.IExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.example.finaldemo.utils.ExcelExportUtils.dataToExcel;
import static com.example.finaldemo.utils.ExcelExportUtils.dataToTxt;


@Service
@RequiredArgsConstructor
public class ExcelExportServiceImpl implements IExcelExportService {
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<InputStreamResource> getDataDownloaded(Long userId, Integer page, Integer size, String fileType) throws IOException {
        Page<User> userPage;
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId"));;
        if (userId == null) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.findUserById(userId, pageable);
        }

        List<User> users = userPage.getContent();
        InputStreamResource resource;
        String fileName;
        MediaType mediaType;

        switch (fileType.toLowerCase()) {
            case "excel":
                ByteArrayInputStream excelData = dataToExcel(users);
                resource = new InputStreamResource(excelData);
                fileName = "users.xlsx";
                mediaType = MediaType.parseMediaType("application/vnd.ms-excel");
                break;
            case "txt":
                ByteArrayInputStream txtData = dataToTxt(users);
                resource = new InputStreamResource(txtData);
                fileName = "example2.txt";
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(mediaType)
                .body(resource);
    }
}
