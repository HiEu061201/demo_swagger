package com.example.finaldemo.services.interfaces;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import java.io.IOException;


public interface IExcelExportService {
    ResponseEntity<InputStreamResource> getDataDownloaded(Long userId, Integer page, Integer size,String fileType) throws IOException;
}
