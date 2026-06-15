package com.example.EmployeeAttendanceApp.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Optional<User> userOpt = userService.login(email, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("loginUserId", user.getId());
            session.setAttribute("loginUserName", user.getName());
            session.setAttribute("role", user.getRole());
            //return "redirect:/top";
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/top";
            }
            return "redirect:/top";
        }

        model.addAttribute("error", "メールアドレスまたはパスワードが違います。");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
