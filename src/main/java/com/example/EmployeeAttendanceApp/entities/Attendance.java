package com.example.EmployeeAttendanceApp.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDate workDate;

    private LocalTime clockIn;
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private LocalTime clockOut;

    private String workingHours;
    private String breakHours;
    private String status;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public LocalTime getClockIn() {
        return clockIn;
    }

    public LocalTime getBreakStart() {
        return breakStart;
    }

    public LocalTime getBreakEnd() {
        return breakEnd;
    }

    public LocalTime getClockOut() {
        return clockOut;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public String getBreakHours() {
        return breakHours;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public void setClockIn(LocalTime clockIn) {
        this.clockIn = clockIn;
    }

    public void setBreakStart(LocalTime breakStart) {
        this.breakStart = breakStart;
    }

    public void setBreakEnd(LocalTime breakEnd) {
        this.breakEnd = breakEnd;
    }

    public void setClockOut(LocalTime clockOut) {
        this.clockOut = clockOut;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public void setBreakHours(String breakHours) {
        this.breakHours = breakHours;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
