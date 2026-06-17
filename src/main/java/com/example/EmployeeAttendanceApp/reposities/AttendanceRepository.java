package com.example.EmployeeAttendanceApp.reposities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EmployeeAttendanceApp.entities.Attendance;
/**
 * 勤怠情報を操作するRepository
 * JpaRepositoryを継承することでCRUD機能を利用できる
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	List<Attendance> findByUserIdOrderByWorkDateDesc(Long userId);
	/**
	 * 指定したユーザーの勤怠履歴を取得する
	 * workDateの降順（新しい日付順）で並び替える
	 */   
	
	List<Attendance> findByUserIdAndWorkDateBetweenOrderByWorkDateDesc(
	        Long userId,
	        LocalDate startDate,
	        LocalDate endDate);
	
	List<Attendance> findByName(String keyword);
	List<Attendance> findByStatus(String status);
}
