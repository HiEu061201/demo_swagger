package com.example.finaldemo.services.impl;

import com.example.finaldemo.exception.InvalidDataException;
import com.example.finaldemo.entities.User;
import com.example.finaldemo.exception.InvalidUserIdException;
import com.example.finaldemo.services.interfaces.IExcelUploadService;
import com.example.finaldemo.utils.StringDealer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class ExcelUploadServiceImpl implements IExcelUploadService {
    private static final StringDealer stringDealer = new StringDealer();

    @Override
    public boolean isValidFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || contentType.equals("text/plain"));
    }

    @Override
    public List<User> getCustomersDataFromExcel(InputStream inputStream)  {
        List<User> users = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("sheetForUserData");
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                User user = new User();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                double numericValue = cell.getNumericCellValue();
                                long userId = (long) numericValue;
                                user.setUserId(userId);
                            } else {
                                throw new InvalidUserIdException("Invalid userId format at row " + (rowIndex) + ": userId must have a valid format.");
                            }
                            break;
                        case 1:
                            String email = "";
                            if (cell.getCellType() == CellType.STRING) {
                                email = cell.getStringCellValue();
                            }
                            if (stringDealer.checkEmailRegex(email)) {
                                user.setEmail(email);
                            } else {
                                throw new InvalidDataException("Invalid email format at row " + (rowIndex + 1) + ": Email must have a valid format.");
                            }
                            break;
                        case 2:
                            user.setPassword(cell.getStringCellValue());
                            break;
                        case 3:
                            user.setRole(cell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIndex++;
                }
                users.add(user);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getUserDataFromTxt(InputStream inputStream) {
        List<User> users = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                User user = new User();
                user.setUserId(Long.valueOf(data[0]));
                user.setEmail(data[1]);
                user.setPassword(data[2]);
                user.setRole(data[3]);
                users.add(user);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
