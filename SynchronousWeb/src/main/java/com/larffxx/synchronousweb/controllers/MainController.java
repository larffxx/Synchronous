package com.larffxx.synchronousweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("/")
    public String main(){
        return "pages/main";
    }

    @GetMapping("/invite")
    public String invite(){
        return "pages/invite";
    }

    @GetMapping("/premium")
    public String premium(){
        return "pages/premium";
    }
}
