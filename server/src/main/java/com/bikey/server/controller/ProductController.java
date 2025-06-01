package com.bikey.server.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bikey.server.model.Product;
import com.bikey.server.repository.ProductRepository;
import com.bikey.server.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @PostMapping("/register")
    public String insert_product(@RequestBody Map<String, String> regiData) {
        productService.register(regiData);
        return "등록완료 되었습니다.";
    }

    @PostMapping("/registerfile")
    public String insertfile_product(@RequestParam("file") MultipartFile file) {
        try {
            String result = productService.register_file(file);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "엑셀 처리 실패";
        }
    }

    @GetMapping("/getProductAll")
    public List<Product> getMethodName() {
        return productRepository.findAll();
    }
}
