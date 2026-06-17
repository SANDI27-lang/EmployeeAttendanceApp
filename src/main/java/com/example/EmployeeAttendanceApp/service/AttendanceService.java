package com.example.EmployeeAttendanceApp.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.EmployeeAttendanceApp.entities.Attendance;
import com.example.EmployeeAttendanceApp.reposities.AttendanceRepository;
/**
 * クラス名：AttendanceService
 * 役割：勤怠情報に関するビジネスロジックを担当する
 * 主な機能：勤怠登録、勤務時間計算、休憩時間計算、実働時間計算、勤怠履歴取得
 * 利用Repository：AttendanceRepository
 * 備考：登録時にステータスを「申請中」に設定し、管理者承認後に「承認済」へ変更する
 */

/**サービスクラスとしてSpringに登録**/
@Service
public class AttendanceService {
	/**勤怠情報を操作するRepository**/
    private final AttendanceRepository attendanceRepository;
    /**コンストラクタインジェクション**/
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    /**勤怠情報の登録処理**/
    public Attendance save(Attendance attendance) {
    	 /**勤務時間を計算**/
        Duration work = Duration.between(attendance.getClockIn(), attendance.getClockOut());
        /** 休憩時間を計算**/
        Duration rest = Duration.between(attendance.getBreakStart(), attendance.getBreakEnd());
        /**実働時間 = 勤務時間 - 休憩時間**/
        Duration actual = work.minus(rest);
        /** 休憩時間を設定**/
        attendance.setBreakHours(format(rest));
        /**実働時間を設定**/
        attendance.setWorkingHours(format(actual));
        /** 初期ステータスを設定**/
        attendance.setStatus("申請中");
        /** DBへ保存**/
        return attendanceRepository.save(attendance);
    }
    /** 勤怠履歴を全件取得 */
    public List<Attendance> getHistory(Long userId) {
        return attendanceRepository.findByUserIdOrderByWorkDateDesc(userId);
    }
    
    /** 月別の勤怠履歴取得 */
    public List<Attendance> getHistoryByMonth(Long userId, int year, int month) {
    	/** 検索開始日を作成 例：2026-06-01 **/
    	LocalDate startDate = LocalDate.of(year, month, 1);
    	/** 検索終了日を作成 例：2026-06-30 **/
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        /** 指定期間内の勤怠履歴を取得 **/
        return attendanceRepository.findByUserIdAndWorkDateBetweenOrderByWorkDateDesc(
                        userId,
                        startDate,
                        endDate);

    }
    /**
     * 時間表示変換
     * 例：Duration型の「PT8H30M」を「8時間30分」に変換して表示する
     */
    private String format(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return hours + "時間" + minutes + "分";
    }
}
