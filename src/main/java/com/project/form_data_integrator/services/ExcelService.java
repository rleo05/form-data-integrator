package com.project.form_data_integrator.services;

import com.project.form_data_integrator.dto.RegistrationDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ExcelService {
    private static final String FILE_LOCATION = "C:\\Users\\leona\\OneDrive\\Documentos\\dev\\excel\\testexcel.xlsx";

    public boolean checkEmail(String email) throws IOException {
        if(!new File(FILE_LOCATION).exists()){
            createNewSheet();
        }
        try(FileInputStream fis = new FileInputStream(FILE_LOCATION);
            XSSFWorkbook workbook = new XSSFWorkbook(fis)){
            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean isFound = false;

            for(Row row : sheet){
                Cell cell = row.getCell(3);
                if(cell.getCellType() == CellType.STRING &&
                        cell.getStringCellValue().equals(email)){
                    isFound = true;
                    break;
                }
            }
            return isFound;
        }
    }

    private void createNewSheet() throws IOException {
        try(XSSFWorkbook workbook = new XSSFWorkbook()){
            XSSFSheet sheet = workbook.createSheet("Users Table");

            sheet.setColumnWidth(0, 7500);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 7000);
            sheet.setColumnWidth(4, 4000);

            Row row = sheet.createRow(0);
            Cell nameCell = row.createCell(0);
            Cell phoneCell = row.createCell(1);
            Cell countryCell = row.createCell(2);
            Cell emailCell = row.createCell(3);
            Cell passwordCell = row.createCell(4);

            CellStyle style = workbook.createCellStyle();

            XSSFColor lightBlueColor = new XSSFColor(IndexedColors.AQUA, null);
            style.setFillForegroundColor(lightBlueColor);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font font = workbook.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);

            nameCell.setCellStyle(style);
            phoneCell.setCellStyle(style);
            countryCell.setCellStyle(style);
            emailCell.setCellStyle(style);
            passwordCell.setCellStyle(style);

            nameCell.setCellValue("Name");
            phoneCell.setCellValue("Phone");
            countryCell.setCellValue("Country");
            emailCell.setCellValue("E-mail");
            passwordCell.setCellValue("Password");


            try(FileOutputStream fos = new FileOutputStream(FILE_LOCATION)){
                workbook.write(fos);
            }
        }
    }

    public void registerNewUser(RegistrationDTO registrationDTO) throws IOException {
        try(FileInputStream fis = new FileInputStream(FILE_LOCATION);
        XSSFWorkbook workbook = new XSSFWorkbook(fis)){
            XSSFSheet sheet = workbook.getSheetAt(0);

            int lastRowNum = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRowNum+1);

            row.createCell(0).setCellValue(registrationDTO.name());
            row.createCell(1).setCellValue(registrationDTO.phone());
            row.createCell(2).setCellValue(registrationDTO.country());
            row.createCell(3).setCellValue(registrationDTO.email());
            row.createCell(4).setCellValue(registrationDTO.password());

            try(FileOutputStream fos = new FileOutputStream(FILE_LOCATION)){
                workbook.write(fos);
            }
        }
    }
}
