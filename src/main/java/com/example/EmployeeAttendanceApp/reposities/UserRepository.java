package com.example.EmployeeAttendanceApp.reposities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.EmployeeAttendanceApp.entities.User;
/**
 * Userエンティティを操作するRepository
 * JpaRepositoryを継承することでCRUD機能を利用できる
 */
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * メールアドレスとパスワードでユーザーを検索する
	 * ログイン認証時に使用
	 */   
	Optional<User> findByEmailAndPassword(String email, String password);
	Optional<User> findByEmail(String email);
	
	List<User> findByNameContainingOrEmailContaining(String name, String email);

}
