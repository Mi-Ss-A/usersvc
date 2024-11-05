package com.wibeechat.missa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping("/welcome")
    public String showWelcomePage() {
        return "welcome-loading";
    }

    @GetMapping("/page1")
    public String showPage1() {
        return "page1"; // next-page.html 템플릿 반환
    }

    @GetMapping("/page2")
    public String showPage2() {
        return "page2"; // next-page.html 템플릿 반환
    }
    
}
