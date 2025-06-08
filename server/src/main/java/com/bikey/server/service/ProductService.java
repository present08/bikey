package com.bikey.server.service;

import java.io.InputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bikey.server.model.BikeyProduct;
import com.bikey.server.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public BikeyProduct register(Map<String, String> resiData) {
        BikeyProduct product = new BikeyProduct();
        product.setDivision(resiData.get("division").replace(" ", ""));
        product.setProductName(resiData.get("productName").replace(" ", ""));
        product.setTransName(resiData.get("transName").replace(" ", ""));
        productRepository.save(product);
        return product;
    }

    public String register_file(MultipartFile file) throws Exception {
        // 엑셀파일 Load
        InputStream inputStream = file.getInputStream();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell divCell = row.getCell(0); // A열
                Cell p_nameCell = row.getCell(1); // B열
                Cell t_nameCell = row.getCell(2); // B열
                String divText = divCell.getStringCellValue().replace(" ", "");
                String p_nameText = p_nameCell.getStringCellValue().replace(" ", "");
                String t_nameText = t_nameCell.getStringCellValue().replace(" ", "");
                BikeyProduct product = new BikeyProduct();
                product.setDivision(divText);
                product.setProductName(p_nameText);
                product.setTransName(t_nameText);
                productRepository.save(product);
            }
            return "등록완료";
        }
    }
}