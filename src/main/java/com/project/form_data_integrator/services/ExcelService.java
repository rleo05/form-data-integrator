package com.project.form_data_integrator.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ExcelService {
    private static final String FILE_LOCATION = "C:\\Users\\leona\\OneDrive\\Documentos\\dev\\excel\\testexcel.xlsx";

    public boolean checkEmail(String email) throws IOException {
        if(!new File(FILE_LOCATION).exists()){
            createNewSheet();
        }
        return true;
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
}
