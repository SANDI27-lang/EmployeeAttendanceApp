package com.example.EmployeeAttendanceApp.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.EmployeeAttendanceApp.entities.Attendance;
import com.example.EmployeeAttendanceApp.service.AttendanceService;

import jakarta.servlet.http.HttpSession;
/**
 * クラス名：AttendanceController
 * 役割：社員の勤怠登録、勤怠履歴表示、登録完了画面表示を担当するコントローラー
 * 使用アノテーション：@Controller
 * 利用サービス：AttendanceService
 * セッション利用：loginUserId
 * 主な機能：出勤・退勤登録、勤怠履歴表示、ログインチェック
 */

/**コントローラークラスとしてSpringに登録**/
@Controller
public class AttendanceController {

    private final AttendanceService attendanceService;
    /**コンストラクタインジェクション**/
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**出勤・退勤画面表示**/
    @RequestMapping(value = "/attendance", method = RequestMethod.GET)
    public String attendancePage(HttpSession session, Model model) {
    	/** セッションからログインユーザーIDを取得**/
        Long userId = (Long) session.getAttribute("loginUserId");
        /** 未ログインの場合はログイン画面へ戻す**/
        if (userId == null) {
            return "redirect:/login";
        }
        /**勤怠情報オブジェクトを生成**/
        Attendance attendance = new Attendance();
        /** 初期値として本日の日付を設定**/
        attendance.setWorkDate(LocalDate.now());
        /** 初期表示用の出勤時間**/
        attendance.setClockIn(LocalTime.of(9, 0));
        /** 初期表示用の休憩開始時間**/
        attendance.setBreakStart(LocalTime.of(12, 0));
        /** 初期表示用の休憩終了時間**/
        attendance.setBreakEnd(LocalTime.of(13, 0));
        /** 初期表示用の退勤時間**/
        attendance.setClockOut(LocalTime.of(18, 0));
        /** 画面へ勤怠情報を渡す**/
        model.addAttribute("attendance", attendance);
        /** attendance.htmlを表示**/
        return "attendance";
    }

    /**勤怠登録処理**/
    @RequestMapping(value = "/attendance/add", method = RequestMethod.POST)
    public String addAttendance(@ModelAttribute Attendance attendance, HttpSession session) {
    	/** セッションからログインユーザーIDを取得**/
        Long userId = (Long) session.getAttribute("loginUserId");
        /** 未ログインの場合はログイン画面へ戻す**/
        if (userId == null) {
            return "redirect:/login";
        }
        /** 勤怠情報にログインユーザーIDを設定**/
        attendance.setUserId(userId);
        /** 勤怠情報をDBへ保存**/
        attendanceService.save(attendance);
        /** 登録完了画面へ遷移**/
        return "redirect:/result";
    }

    /** 勤怠履歴画面表示**/
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String history(HttpSession session, Model model, 
    		@RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
    	/** セッションからログインユーザーIDを取得**/
        Long userId = (Long) session.getAttribute("loginUserId");
        /** 未ログインの場合はログイン画面へ戻す**/
        if (userId == null) {
            return "redirect:/login";
        }
        /** 勤怠履歴格納用リスト**/
        List<Attendance> attendanceList;
        /** 年・月が入力されている場合**/
        if (year != null && month != null) {
        	/** 指定された年月の勤怠履歴を取得**/
            attendanceList = attendanceService.getHistoryByMonth(userId, year, month);
        } else {
        	/** 全件取得**/
            attendanceList = attendanceService.getHistory(userId);
        }
        /** ログインユーザーの勤怠履歴を取得**/
        //List<Attendance> attendanceList = attendanceService.getHistory(userId);
        
        /** 勤怠履歴を画面へ渡す**/
        model.addAttribute("attendanceList", attendanceList);
        /** 入力された年を保持**/
        model.addAttribute("year", year);
        /** 入力された月を保持**/
        model.addAttribute("month", month);
        /**history.htmlを表示**/
        return "history";
    }

    /** 登録完了画面表示**/
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String result() {
    	/** result.htmlを表示**/
        return "result";
    }
}