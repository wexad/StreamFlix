package com.wexad.controller.user;


import com.wexad.dto.SignupDTO;
import com.wexad.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
