package com.bikey.server.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.poifs.crypt.Decryptor;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bikey.server.model.Bikey;
import com.bikey.server.repository.BikeyRepository;
import com.bikey.server.repository.ProductRepository;

@Service
public class BikeyService {
    @Autowired
    private BikeyRepository bikeyRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Bikey> excelProcess(MultipartFile file, String password) throws Exception {
        // 엑셀파일 Load
        InputStream inputStream = file.getInputStream();

        // ApachePOI의 POIFSFileSystem 클래스를 이용하여 엑셀파일의 포맷(암호화방식) 확인
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);

        // EncrypytionInfo 객체를 생성하여 엑셀 파일의 암호화 정보를 확인
        EncryptionInfo info = new EncryptionInfo(fs);

        // EncrypytionInfo를 바탕으로 Decrptor 객체를 생성(복호화 객체)
        Decryptor decryptor = Decryptor.getInstance(info);

        // 사용자가 전달해준 암호와 엑셀파일의 암호를 비교하여 일치하지 않을 경우 에러 리턴
        if (!decryptor.verifyPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }
        // 비밀번호가 일치할 경우 복호화된 엑셀 데이터 Open
        InputStream decryptedStream = decryptor.getDataStream(fs);

        try (Workbook workbook = new XSSFWorkbook(decryptedStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Bikey> result = new ArrayList<>();

            List<String> productList = productRepository.findByProductName("자전거");
            List<String> productTrnasList = productRepository.findByProductTransName("자전거");
            List<String> productDivList = productRepository.findByProductName("제품군");
            List<String> modelDivList = productRepository.findByProductName("모델");
            List<String> colorList = productRepository.findByProductName("색상");
            List<String> optionList = productRepository.findByListProductName(List.of("옵션", "용품", "부품"));
            List<String> optionTrnasList = productRepository.findByListProductTranName(List.of("옵션", "용품", "부품"));

            for (int i = 2, idx = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell nameCell = row.getCell(1); // B열
                Cell phoneCell = row.getCell(2); // C열
                Cell modelCell = row.getCell(3); // D열
                Cell optionCell = row.getCell(4); // E열
                Cell orderNumCell = row.getCell(15); // P열
                Cell orderDateCell = row.getCell(22); // W열

                // if (nameCell == null || phoneCell == null || modelCell == null)
                // continue;
                // if (nameCell.getCellType() != CellType.STRING || phoneCell.getCellType() !=
                // CellType.STRING
                // || modelCell.getCellType() != CellType.STRING)
                // continue;

                String name = nameCell.getStringCellValue().replace(" ", "");
                String phone = phoneCell.getStringCellValue().replace(" ", "");
                String model = modelCell.getStringCellValue().replace(" ", "");
                String option = optionCell.getStringCellValue().replace(" ", "");
                String orderNum = orderNumCell.getStringCellValue().replace(" ", "");
                LocalDate orderDate = orderDateCell.getLocalDateTimeCellValue().toLocalDate();
                // String model_option_div = productDivList.stream()
                // .anyMatch(model::equals) ? "화물" : "용품";
                String model_option_div = productList.stream()
                        .anyMatch(model::equals) ? "화물" : "용품";

                // 모델 분류
                int matched_product_idx = productList.indexOf(model);
                // product이름을 담을 변수 설정
                String product_name = "";
                if (matched_product_idx != -1) {
                    // 모델명 입력
                    product_name = productTrnasList.get(matched_product_idx);

                    // 옵션에 모델명이 있는 경우 재검색
                    String research_product = productDivList.stream()
                            .filter(option::contains)
                            .max(Comparator.comparingInt(String::length))
                            .orElse("");
                    if (research_product != "") {
                        String research_model = modelDivList.stream()
                                .filter(option::contains)
                                .max(Comparator.comparingInt(String::length))
                                .orElse("");
                        product_name = research_product + research_model;
                    }
                }

                // 옵션 분류
                int matched_option_idx = optionList.indexOf(option);
                System.out.println(matched_option_idx + option);
                String option_name = "";
                if (matched_option_idx != -1) {
                    option_name = optionTrnasList.get(matched_option_idx);
                }

                String color_name = "";
                // 옵션 분류 ( 색상 )
                if (product_name != "") {
                    String color_matched = colorList.stream()
                            .filter(option::contains)
                            .max(Comparator.comparingInt(String::length))
                            .orElse("");
                    color_name = color_matched;
                }

                String assembly = "";
                // 옵션 분류 ( 조립 )
                if (option.contains("완조립")) {
                    assembly = "/완";
                } else if (option.contains("반조립")) {
                    assembly = "/반";
                }

                // 이 if문에는 추가만 하는거야
                if (result.size() > 0 && result.get(idx - 1).getOrder_num() == Long.parseLong(orderNum)) {
                    Bikey prev_bikey = result.get(idx - 1);
                    if (model_option_div == "화물") {
                        prev_bikey.setDivision(model_option_div);
                        prev_bikey.setModel(product_name);
                        prev_bikey.setModel_option(color_name + assembly + "/" + prev_bikey.getModel_option());
                    } else {
                        prev_bikey.setModel_option(prev_bikey.getModel_option() + "/" + option_name);
                    }
                } else {
                    Bikey order = new Bikey();

                    order.setName(name);
                    order.setPhone(phone);
                    order.setOrder_num(Long.parseLong(orderNum));
                    order.setDivision(model_option_div);
                    order.setOrder_date(orderDate);
                    if (model_option_div == "화물") {
                        order.setModel(product_name);
                        order.setModel_option(color_name + assembly);
                    } else {
                        order.setModel_option(option_name);
                    }

                    result.add(order);
                    idx++;
                }
            }

            return result;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
