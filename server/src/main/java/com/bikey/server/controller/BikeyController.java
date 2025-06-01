package com.bikey.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bikey.server.model.Bikey;
import com.bikey.server.service.BikeyService;

@RestController
@RequestMapping("/api")
public class BikeyController {
    @Autowired
    private BikeyService bikeyService;

    @PostMapping("/excelupload")
    public List<Bikey> uploadFile(@RequestParam("file") MultipartFile file,
            @RequestParam("password") String password) {
        try {
            List<Bikey> result = bikeyService.excelProcess(file, password);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
