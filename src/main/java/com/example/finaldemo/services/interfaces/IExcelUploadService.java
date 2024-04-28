package com.example.finaldemo.services.interfaces;

import com.example.finaldemo.exception.InvalidDataException;
import com.example.finaldemo.entities.User;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;

public interface IExcelUploadService {

    boolean isValidFile(MultipartFile file);

     List<User> getCustomersDataFromExcel(InputStream inputStream) throws InvalidDataException;

    List<User> getUserDataFromTxt(InputStream inputStream);
}
