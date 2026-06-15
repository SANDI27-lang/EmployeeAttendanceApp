package com.example.EmployeeAttendanceApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class TopController {

    @GetMapping("/top")
    public String top(HttpSession session, Model model) {

        Object userId = session.getAttribute("loginUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        model.addAttribute("userName", session.getAttribute("loginUserName"));
        return "top";
    }
}