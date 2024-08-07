package com.wexad.controller;

import com.wexad.security.CustomUserDetailsService;
import com.wexad.security.SecurityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(Model model) {
        UserDetails user = SecurityUtils.getUser();
        model.addAttribute("user", user);
        return "main/index";
    }
}
