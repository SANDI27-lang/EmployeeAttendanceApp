package com.example.EmployeeAttendanceApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/**
 * 社員用トップページの表示を行う。
 * セッション情報を利用してログイン状態を確認する。
 * 未ログインの場合はログイン画面へリダイレクトする。
 * ログインユーザー名を取得し、トップ画面へ表示する。
 */
@Controller
public class TopController {
	/**トップページを表示する**/
    @RequestMapping("/top")
    public String top(HttpSession session, Model model) {
    	/**セッションからログインユーザーIDを取得**/
        Object userId = session.getAttribute("loginUserId");
        /**ログインしていない場合はログイン画面へ戻す**/
        if (userId == null) {
            return "redirect:/login";
        }
        /**ログインユーザー名を取得し、画面へ渡す(eg.ようこそ、○○さん)**/
        model.addAttribute("userName", session.getAttribute("loginUserName"));
        /**top.html を表示**/
        return "top";
    }
}