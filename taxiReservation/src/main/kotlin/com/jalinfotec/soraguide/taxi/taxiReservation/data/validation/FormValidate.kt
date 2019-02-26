package com.jalinfotec.soraguide.taxi.taxiReservation.data.validation

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import java.sql.Time
import java.util.*


class FormValidate {
    companion object {
        const val mailErrorMessage: String = "入力されたメールアドレスが一致しません"
        const val dateErrorMassage: String = "乗車日、乗車時間に過去の日時は指定できません"
    }

    /**
     * 登録画面から確認画面遷移時のバリデートチェック
     */
    fun registrationCheck(rsvForm: ReservationForm): String {
        if (!rideOnDateValidate(rsvForm.date, rsvForm.time)) {
            return dateErrorMassage
        }

        if (!mailValidate(rsvForm.mail, rsvForm.mailCheck)) {
            return mailErrorMessage
        }

        return ""
    }

    /**
     * 確認画面から登録完了画面遷移時のバリデートチェック
     */
    fun confirmCheck(rsvForm: ReservationForm): String {
        if (!rideOnDateValidate(rsvForm.date, rsvForm.time)) {
            return dateErrorMassage
        }
        return ""
    }

    /**
     * 変更入力画面から変更完了画面遷移時のバリデートチェック
     */
    fun changeCheck(changeForm: ChangeForm): String {
        if (!rideOnDateValidate(changeForm.date, changeForm.time)) {
            return dateErrorMassage
        }

        return ""
    }

    /**
     * メールの一致チェック
     */
    private fun mailValidate(mail: String, mailCheck: String): Boolean {
        //メール同一チェック
        return if (mail != mailCheck) {
            println("メール不一致")
            false
        } else true
    }

    /**
     * 搭乗日の妥当性チェック
     */
    private fun rideOnDateValidate(date: Date, timeStr: String): Boolean {
        val nowDate = Calendar.getInstance()
        nowDate.set(Calendar.HOUR_OF_DAY, 0)
        nowDate.set(Calendar.MINUTE, 0)
        nowDate.set(Calendar.SECOND, 0)
        nowDate.set(Calendar.MILLISECOND, 0)

        //日付の過去日チェック
        if (date.before(nowDate.time)) {
            println("【ERROR】乗車日が過去日")
            return false
        } else if (date == nowDate.time) {
            //今日の日付を指定している場合のみ、時間もチェックする
            val nowTime = Time(System.currentTimeMillis()).toLocalTime()
            val time = Time.valueOf("$timeStr:00").toLocalTime()
            println("現在時刻:$nowTime")
            println("入力された時刻:$time")
            //時間の過去チェック
            if(time.isBefore(nowTime)){
                println("【ERROR】乗車時間を過ぎている")
                return false
            }
        }
        return true
    }
}