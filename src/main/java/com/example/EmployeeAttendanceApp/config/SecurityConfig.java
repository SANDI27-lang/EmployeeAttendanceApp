package com.example.EmployeeAttendanceApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.reposities.UserRepository;

/** 
 * クラス名：SecurityConfig * 
 * 役割 ：Spring Securityの設定を管理するクラス。 
 * ログイン認証、アクセス制御、ログアウト処理を設定する。 
 * 主な機能： 
 * ・ログイン認証 
 * ・権限管理（USER / ADMIN） 
 * ・ログアウト機能 
 * ・認証ユーザー情報取得 * 
 * 利用クラス： UserRepository 
 * */

@Configuration
public class SecurityConfig {
	/** ユーザー情報取得用Repository**/
    private final UserRepository userRepository;
    /** コンストラクタインジェクション**/
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	/** CSRF対策を無効化 **/
        http.csrf(csrf -> csrf.disable());
        /** URLごとのアクセス権限設定 **/
        http.authorizeHttpRequests(auth -> auth
        		/** ログイン画面は誰でもアクセス可能**/
                .requestMatchers("/login").permitAll()
                /** 管理者画面はADMIN権限のみ**/
                .requestMatchers("/admin/**").hasRole("ADMIN")
                /** 社員画面はUSERまたはADMINが利用可能 **/
                .requestMatchers("/top", "/attendance/**", "/history", "/result")
                    .hasAnyRole("USER", "ADMIN")
                 /** その他は認証必須 **/
                .anyRequest().authenticated()
        );
        /** ログイン設定 **/
        http.formLogin(form -> form
        		/** 独自ログイン画面 **/
                .loginPage("/login")
                /** メールアドレスをユーザー名として使用 **/
                .usernameParameter("email")
                /** パスワード項目名 **/
                .passwordParameter("password")
                /** ログイン成功時処理 **/
                .successHandler((request, response, authentication) -> {
                	/** ログインしたメールアドレス取得 **/
                    String email = authentication.getName();
                    /** 固定管理者 **/
                    if ("admin@example.com".equals(email)) {
                        request.getSession().setAttribute("loginUserId", 0L);
                        request.getSession().setAttribute("loginUserName", "管理者");
                        request.getSession().setAttribute("role", "ADMIN");
                        /** 管理者トップへ遷移**/
                        response.sendRedirect("/admin/top");
                        return;
                    }
                    /** DBからユーザー取得 **/
                    User user = userRepository.findByEmail(email).orElseThrow();
                    /** セッションへ保存 **/
                    request.getSession().setAttribute("loginUserId", user.getId());
                    request.getSession().setAttribute("loginUserName", user.getName());
                    request.getSession().setAttribute("role", user.getRole());
                    /** 社員トップへ遷移 **/
                    response.sendRedirect("/top");
                })
                .permitAll()
        );
        /** ログアウト設定 **/
        http.logout(logout -> logout
        		/** ログアウトURL **/
                .logoutUrl("/logout")
                /** ログアウト成功後 **/
                .logoutSuccessUrl("/login")
                .permitAll()
        );

        return http.build();
    }
    /** 
     * ユーザー認証処理 
     * ・固定管理者認証 
     * ・DBユーザー認証 
     * */
    @Bean
    public UserDetailsService userDetailsService() {

        return email -> {
        	/** 固定管理者 **/
            if ("admin@example.com".equals(email)) {
                return org.springframework.security.core.userdetails.User
                        .withUsername("admin@example.com")
                        .password("1234")
                        .roles("ADMIN")
                        .build();
            }
            /** DBからユーザー取得 **/
            User user = userRepository.findByEmail(email).orElseThrow();
            /** Spring Security用ユーザー作成 **/
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    java.util.List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole())
                    )
            );
        };
    }
    /** パスワード設定 
     * NoOpPasswordEncoder → パスワードを暗号化しない （研修用）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}