package com.example.EmployeeAttendanceApp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.EmployeeAttendanceApp.entities.Attendance;
import com.example.EmployeeAttendanceApp.entities.User;
import com.example.EmployeeAttendanceApp.reposities.AttendanceRepository;
import com.example.EmployeeAttendanceApp.reposities.UserRepository;

@Controller
public class AdminController {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private UserRepository userRepository;

	// 管理者ホーム
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView showAdminHome(ModelAndView mav) {

		mav.setViewName("admin-home");
		mav.addObject("title", "管理者ホーム");

		return mav;
	}

	// 社員一覧
	@RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
	public ModelAndView showUserList(ModelAndView mav) {

		mav.setViewName("user-list");
		mav.addObject("title", "社員一覧");

		Iterable<User> list = userRepository.findAll();
		mav.addObject("users", list);

		return mav;
	}

	// 社員登録画面
	@RequestMapping(value = "/admin/user/register", method = RequestMethod.GET)
	public ModelAndView createUser(@ModelAttribute("formModel") User user, ModelAndView mav) {

		mav.setViewName("user-register");
		mav.addObject("title", "社員登録");

		return mav;
	}

	// 社員登録処理
	@RequestMapping(value = "/admin/user/register", method = RequestMethod.POST)
	public ModelAndView saveUser(@ModelAttribute("formModel") User user, ModelAndView mav) {
		user.setRole("USER");
		userRepository.saveAndFlush(user);

		return new ModelAndView("redirect:/admin/user/list");
	}

	// 社員編集画面
	@RequestMapping(value = "/admin/user/update/{id}", method = RequestMethod.GET)
	public ModelAndView updateUser(@PathVariable int id, ModelAndView mav) {

		mav.setViewName("user-edit");
		mav.addObject("title", "社員編集");

		Optional<User> data = userRepository.findById((long) id);

		mav.addObject("formModel", data.get());

		return mav;
	}

	// 社員編集処理
	@RequestMapping(value = "/admin/user/update", method = RequestMethod.POST)
	public ModelAndView updateUser(@ModelAttribute("formModel") User user, ModelAndView mav) {

		userRepository.saveAndFlush(user);

		return new ModelAndView("redirect:/admin/user/list");
	}

	// 社員削除確認画面
	@RequestMapping(value = "/admin/user/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteUser(@PathVariable int id, ModelAndView mav) {

		mav.setViewName("user-delete");
		mav.addObject("title", "社員削除");
		mav.addObject("msg", "削除してもよろしいですか？");

		Optional<User> data = userRepository.findById((long) id);

		mav.addObject("formModel", data.get());

		return mav;
	}

	// 社員削除処理
	@RequestMapping(value = "/admin/user/delete", method = RequestMethod.POST)
	public ModelAndView removeUser(@RequestParam long id, ModelAndView mav) {

		userRepository.deleteById(id);

		return new ModelAndView("redirect:/admin/user/list");
	}

	// 勤怠一覧
	@RequestMapping(value = "/admin/attendance/list", method = RequestMethod.GET)
	public ModelAndView showAttendanceList(ModelAndView mav) {

		mav.setViewName("attendance-list");
		mav.addObject("title", "勤怠管理一覧");

		Iterable<Attendance> attendanceList = attendanceRepository.findAll();
		Iterable<User> users = userRepository.findAll();
		mav.addObject("attendanceList", attendanceList);
		mav.addObject("users", users);

		return mav;
	}

	// 勤怠詳細
	@RequestMapping(value = "/admin/attendance/detail/{id}", method = RequestMethod.GET)
	public ModelAndView showAttendanceDetail(@PathVariable int id, ModelAndView mav) {

		mav.setViewName("attendance-detail");
		mav.addObject("title", "勤怠詳細");

		Optional<Attendance> data = attendanceRepository.findById((long) id);

		mav.addObject("attendance", data.get());

		return mav;
	}

	// 勤怠承認
	@RequestMapping(value = "/admin/attendance/approve/{id}", method = RequestMethod.POST)
	public ModelAndView approveAttendance(@PathVariable Long id) {

		Attendance attendance = attendanceRepository.findById(id).orElseThrow();

		attendance.setStatus("承認済");

		attendanceRepository.saveAndFlush(attendance);

		return new ModelAndView("redirect:/admin/attendance/list");
	}

	@RequestMapping(value = "/admin/user/search", method = RequestMethod.GET)
	public ModelAndView searchUser(@RequestParam(value = "keyword", required = false) String keyword,
			ModelAndView mav) {

		mav.setViewName("user-list");

		Iterable<User> list;

		if (keyword == null || keyword.isEmpty()) {
		    mav.addObject("error", "検索キーワードを入力してください");
			// 未入力 → 全件表示
			list = userRepository.findAll();
		} else {
			// 検索あり
			list = userRepository.findByNameContainingOrEmailContaining(keyword, keyword);
		}

		mav.addObject("users", list);
		return mav;
	}
}