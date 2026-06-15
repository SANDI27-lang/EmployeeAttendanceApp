package com.example.EmployeeAttendanceApp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.reposities.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
