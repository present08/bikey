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
import com.bikey.server.repository.ProductRepository;

@Service
public class BikeyService {
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

            List<String> bikeList = productRepository.findByProductName("자전거");
            List<String> productList = productRepository.findByListProductName(List.of("자전거", "용품"));
            List<String> productTransList = productRepository.findByListProductTranName(List.of("자전거", "용품"));
            List<String> productResearchList = productRepository.findByProductName("제품군");
            List<String> optionList = productRepository.findByListProductName(List.of("필수옵션", "선택옵션"));
            List<String> optionTransList = productRepository.findByListProductTranName(List.of("필수옵션", "선택옵션"));

            // List<String> allProductList = productRepository.findAllByProductName();
            // List<String> allTransList = productRepository.findAllByTransName();
            System.out.println("옵션 리스트1 길이 : " + optionList.size());
            System.out.println("옵션 리스트2 길이 : " + optionTransList.size());

            for (int i = 2, idx = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell nameCell = row.getCell(1); // B열
                Cell phoneCell = row.getCell(2); // C열
                Cell modelCell = row.getCell(3); // D열
                Cell optionCell = row.getCell(4); // E열
                Cell orderNumCell = row.getCell(15); // P열
                Cell orderDateCell = row.getCell(22); // W열

                String name = nameCell.getStringCellValue().replace(" ", "");
                String phone = phoneCell.getStringCellValue().replace(" ", "");
                String model = modelCell.getStringCellValue().replace(" ", "");
                String option = optionCell.getStringCellValue().replace(" ", "");
                String orderNum = orderNumCell.getStringCellValue().replace(" ", "");
                LocalDate orderDate = orderDateCell.getLocalDateTimeCellValue().toLocalDate();

                // 화물 구분
                String delivery = bikeList.stream().anyMatch(model::equals) ? "화물" : "용품";
                System.out.println("구분 : " + delivery);

                // 모델 분류
                int matched_product_idx = productList.indexOf(model);
                String product_name = "";

                // 제품리스트에 매칭이 있을 경우 변환 리스트에 맞는 값 리턴
                if (matched_product_idx != -1) {
                    product_name = productTransList.get(matched_product_idx);
                    System.out.println("선택된 제품명 : " + product_name);
                }
                // 옵션 분류
                int matched_option_idx = optionList.indexOf(option);
                System.out.println("Option Num: " + matched_option_idx + ", Option Name: " + option);
                String option_name = "";

                // 옵션리스트에 매칭이 있을 경우 변환 리스트에 맞는 값 리턴
                if (matched_option_idx != -1) {
                    option_name = optionTransList.get(matched_option_idx);
                    System.out.println("선택된 옵션 : " + option_name);
                }

                String research_product = productResearchList.stream()
                        .filter(option_name::contains)
                        .max(Comparator.comparingInt(String::length))
                        .orElse("");
                System.out.println("재검색: " + research_product);

                if (research_product != "" && delivery == "화물") {
                    product_name = option_name;
                    option_name = "";
                }

                // 이 if문에는 추가만 하는거야
                if (result.size() > 0 && result.get(idx - 1).getOrder_num() == Long.parseLong(orderNum)) {
                    System.out.println("Result Size: " + result.size() + ", 주문번호가 이전과 동일 " + Long.parseLong(orderNum));
                    Bikey prev_bikey = result.get(idx - 1);
                    System.out.println("제품 명이 뭘까요 : " + product_name);
                    prev_bikey.setModel(product_name);

                    if (delivery == "화물") {
                        prev_bikey.setDivision(delivery);
                        prev_bikey.setModel_option(prev_bikey.getModel_option());
                        System.out.println("화물 택배: " + prev_bikey);
                    } else {
                        if (prev_bikey.getModel_option() != "") {
                            prev_bikey.setModel_option(prev_bikey.getModel_option() + "/" + option_name);
                        } else {
                            prev_bikey.setModel_option(option_name);
                        }
                        System.out.println("용품 택배: " + prev_bikey);
                    }
                } else {
                    System.out.println("Result Size: " + result.size() + ", 새로운 주문번호: " + Long.parseLong(orderNum));
                    Bikey order = new Bikey();

                    order.setName(name);
                    order.setPhone(phone);
                    order.setOrder_num(Long.parseLong(orderNum));
                    order.setDivision(delivery);
                    order.setOrderDate(orderDate);
                    order.setModel(product_name);
                    order.setModel_option(option_name);
                    System.out.println(order);

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
