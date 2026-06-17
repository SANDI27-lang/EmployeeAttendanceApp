package com.example.EmployeeAttendanceApp.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.service.UserService;

import jakarta.servlet.http.HttpSession;

/**
 * ログイン画面の表示を行う。
 * メールアドレスとパスワードによる認証処理を行う。
 * ログイン成功時にセッションへユーザー情報を保存する。
 * 権限（ADMIN / USER）によって遷移先を切り替える。
 * ログアウト時にセッションを破棄し、ログイン画面へ遷移する。
 */

@Controller
public class LoginController {
	/**UserServiceを利用してログイン認証を行う**/
	private final UserService userService;
	/**コンストラクタインジェクション**/
	public LoginController(UserService userService) {
		this.userService = userService;
	}
	/**アプリケーション起動時に「/」へアクセスした場合、ログイン画面へリダイレクトする**/
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:/login";
	}
	/**ログイン画面を表示する**/
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	/**ログイン処理を実行する**/
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
		/**メールアドレスとパスワードでユーザー検索**/
		Optional<User> userOpt = userService.login(email, password);
		/**ユーザーが存在する場合**/
		if (userOpt.isPresent()) {

			User user = userOpt.get();
			/**セッションにログイン情報を保存**/
			session.setAttribute("loginUserId", user.getId());
			session.setAttribute("loginUserName", user.getName());
			session.setAttribute("role", user.getRole());
			/**管理者の場合は管理者トップ画面へ遷移**/
			if ("ADMIN".equals(user.getRole())) {
				return "redirect:/admin";
			}
			/**一般社員の場合は社員トップ画面へ遷移**/
			return "redirect:/";
		}
		/**ログイン失敗時はエラーメッセージを表示**/
		model.addAttribute("error", "メールアドレスまたはパスワードが違います。");

		return "login";
	}
	/**ログアウト処理**/
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		/**セッションを破棄**/
		session.invalidate();
		/**ログイン画面へ戻る**/
		return "redirect:/login";
	}
}


