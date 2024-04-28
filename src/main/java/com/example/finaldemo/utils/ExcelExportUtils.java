package com.example.finaldemo.utils;


import com.example.finaldemo.entities.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.List;

public class ExcelExportUtils {
    public static String HEADER[] = {"userId", "email", "password", "role"};
    public static String SHEET_NAME = "sheetForUserData";
    public static ByteArrayInputStream dataToExcel(List<User> userList) throws IOException {
        Workbook workbook  = new XSSFWorkbook();

        ByteArrayOutputStream byteArrayOutputStream  = new ByteArrayOutputStream();
        try {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            Row row = sheet.createRow(0);

            for (int i  =0; i< HEADER.length;i++){

                Cell cell = row.createCell(i);
                cell.setCellValue(HEADER[i]);
            }

            int rowIndex = 1;
            for (User u :userList){
                Row row1 = sheet.createRow(rowIndex);
                rowIndex++;
                row1.createCell(0).setCellValue(u.getUserId());
                row1.createCell(1).setCellValue(u.getEmail());
                row1.createCell(2).setCellValue(u.getPassword());
                row1.createCell(3).setCellValue(u.getRole());
            }

            workbook.write(byteArrayOutputStream);
            return  new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            workbook.close();
            byteArrayOutputStream.close();
        }
    }

    public static ByteArrayInputStream dataToTxt(List<User> userList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
        try {
            for (User user : userList) {
                writer.write(user.getUserId() + "," + user.getEmail() + "," + user.getPassword()+ "," + user.getRole());
                writer.newLine();
            }
        } finally {
            writer.close();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }




}
