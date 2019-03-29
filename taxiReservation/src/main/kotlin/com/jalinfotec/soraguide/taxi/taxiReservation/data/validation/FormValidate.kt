package com.jalinfotec.soraguide.taxi.taxiReservation.data.validation

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import java.sql.Time
import java.util.*
import java.util.regex.Pattern


class FormValidate {
    companion object {
        const val passengerErrorMessage: String = "乗車人数は1人以上で指定してください。"
        const val mailDiscordErrorMessage: String = "入力されたメールアドレスが一致しません。"
        const val mailValidateErrorMessage: String = "メールアドレスの形式が正しくありません。"
        const val dateErrorMessage: String = "乗車日、乗車時間に過去の日時は指定できません。"
    }

    /**
     * 登録画面から確認画面遷移時のバリデートチェック
     */
    fun registrationCheck(rsvForm: ReservationForm): String {
        if (!passengerCheck(rsvForm.adult, rsvForm.child)) {
            return passengerErrorMessage
        }

        if (!rideOnDateValidate(rsvForm.rideOnDate, rsvForm.rideOnTime)) {
            return dateErrorMessage
        }

        if (!mailMatchCheck(rsvForm.mail, rsvForm.mailCheck)) {
            return mailDiscordErrorMessage
        }

        if (!mailValidate(rsvForm.mail)) {
            return mailValidateErrorMessage
        }

        return ""
    }

    /**
     * 確認画面から登録完了画面遷移時のバリデートチェック
     */
    fun confirmCheck(rsvForm: ReservationForm): String {
        if (!passengerCheck(rsvForm.adult, rsvForm.child)) {
            return passengerErrorMessage
        }

        if (!rideOnDateValidate(rsvForm.rideOnDate, rsvForm.rideOnTime)) {
            return dateErrorMessage
        }

        if (!mailValidate(rsvForm.mail)) {
            return mailValidateErrorMessage
        }

        return ""
    }

    /**
     * 変更入力画面から変更完了画面遷移時のバリデートチェック
     */
    fun changeCheck(changeForm: ChangeForm): String {
        if (!passengerCheck(changeForm.adult, changeForm.child)) {
            return passengerErrorMessage
        }

        if (!rideOnDateValidate(changeForm.rideOnDate, changeForm.rideOnTime)) {
            return dateErrorMessage
        }

        if (!mailMatchCheck(changeForm.mail, changeForm.mailCheck)) {
            return mailDiscordErrorMessage
        }

        if (!mailValidate(changeForm.mail)) {
            return mailValidateErrorMessage
        }

        return ""
    }

    /**
     * 乗客数チェック
     */
    private fun passengerCheck(adult: Int, child: Int): Boolean {
        return if (adult + child <= 0) {
            println("最低人数以下")
            false
        } else true
    }

    /**
     * メールの一致チェック
     */
    private fun mailMatchCheck(mail: String, mailCheck: String): Boolean {
        //メール同一チェック
        return if (mail != mailCheck) {
            println("メール不一致")
            false
        } else true
    }

    /**
     * メールの形式チェック
     */
    private fun mailValidate(mail: String): Boolean {
        //メールアドレス形式を正規表現でチェック
        val pattern = Pattern.compile("^(([0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]"
                + "+(\\.[0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]+)*)|(\"[^\"]*\"))"
                + "@[0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]+"
                + "(\\.[0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]+)*$")

        return pattern.matcher(mail).find()
    }

    /**
     * 搭乗日の妥当性チェック
     */
    private fun rideOnDateValidate(date: Date, timeStr: String): Boolean {
        println("引数チェック:$date , $timeStr")
        //今日の日付を取得
        val nowDate = Calendar.getInstance()

        //取得した日付から時間だけ抜き出し
        val nowTime = Time(nowDate.timeInMillis).toLocalTime()

        //日付比較のために、時間パラメータを0に設定
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
            val time = Time.valueOf("$timeStr:00").toLocalTime()
            println("現在時刻:$nowTime")
            println("入力された時刻:$time")
            //時間の過去チェック
            if (time.isBefore(nowTime)) {
                println("【ERROR】乗車時間を過ぎている")
                return false
            }
        }
        return true
    }
}