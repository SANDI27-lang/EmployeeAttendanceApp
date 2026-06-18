/**
 * 処理の流れ
 * LoginController→UserService→UserRepository→usersテーブル検索→ユーザー情報取得→LoginControllerへ返却
 */

package com.example.EmployeeAttendanceApp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.reposities.AttendanceRepository;
import com.example.EmployeeAttendanceApp.reposities.UserRepository;

/**サービスクラスとしてSpringに登録**/
@Service
public class UserService {
	 /**UserテーブルへアクセスするRepository**/
    private final UserRepository userRepository;
    /** 勤怠テーブルへアクセスするRepository **/
    private final AttendanceRepository attendanceRepository;
    /** コンストラクタインジェクション**/
    public UserService(UserRepository userRepository, AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }
    @Transactional
    public void deleteUser(Long userId) {

        /** 勤怠履歴削除**/
        attendanceRepository.deleteByUserId(userId);
        /** ユーザー削除**/
        userRepository.deleteById(userId);
    }
    /**
     * ログイン認証処理
     * メールアドレスとパスワードを使用してユーザーを検索する
     */
    public Optional<User> login(String email, String password) {
    	 /**Repositoryを呼び出してユーザー情報を取得**/
        return userRepository.findByEmailAndPassword(email, password);
    }
}
