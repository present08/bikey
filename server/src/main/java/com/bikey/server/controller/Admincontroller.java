package com.bikey.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/admin")
public class Admincontroller {
    @GetMapping("/test")
    public String adminP() {
        return "admin Controller";
    }

}
