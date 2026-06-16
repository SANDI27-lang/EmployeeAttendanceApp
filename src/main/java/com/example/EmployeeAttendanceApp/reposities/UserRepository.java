package com.example.EmployeeAttendanceApp.reposities;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EmployeeAttendanceApp.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);
}
