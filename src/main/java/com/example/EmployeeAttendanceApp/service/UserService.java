/**
 * 処理の流れ
 * LoginController→UserService→UserRepository→usersテーブル検索→ユーザー情報取得→LoginControllerへ返却
 */

package com.example.EmployeeAttendanceApp.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.reposities.UserRepository;

/**サービスクラスとしてSpringに登録**/
@Service
public class UserService {
	 /**UserテーブルへアクセスするRepository**/
    private final UserRepository userRepository;
    /** コンストラクタインジェクション**/
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
