package com.wexad.controller;


import com.wexad.domains.user.AuthUser;
import com.wexad.dto.LoginDTO;
import com.wexad.dto.SignupDTO;
import com.wexad.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService, UserService userService1) {
        this.userService = userService1;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }


    @GetMapping("/signup")
    public String signupPage() {
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute SignupDTO dto) {
        System.out.println(userService.signup(dto).toString());
        return "redirect:/auth/login";
    }

    @GetMapping("/failed")
    public String failed() {
        return "auth/failed";
    }

}
