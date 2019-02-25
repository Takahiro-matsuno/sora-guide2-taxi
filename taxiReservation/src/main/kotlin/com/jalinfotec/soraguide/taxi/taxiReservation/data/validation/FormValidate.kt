package com.jalinfotec.soraguide.taxi.taxiReservation.data.validation

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import java.util.*

class FormValidate {
    companion object {
        const val mailErrorMessage: String = "入力されたメールアドレスが一致しません"
        const val dateErrorMassage: String = "乗車日に過去の日付は指定できません"
    }

    fun registrationCheck(rsvForm: ReservationForm): String {
        if (!rideOnDateValidate(rsvForm.date)) {
            return dateErrorMassage
        }

        if (!mailValidate(rsvForm.mail, rsvForm.mailCheck)) {
            return mailErrorMessage
        }

        return ""
    }

    fun confirmCheck(rsvForm: ReservationForm): String {
        if (!rideOnDateValidate(rsvForm.date)) {
            return dateErrorMassage
        }
        return ""
    }

    fun changeCheck(changeForm : ChangeForm):String{
        if (!rideOnDateValidate(changeForm.date)) {
            return dateErrorMassage
        }

        return ""
    }

    private fun mailValidate(mail: String, mailCheck: String): Boolean {
        //メール同一チェック
        return if (mail != mailCheck) {
            println("メール不一致")
            false
        } else true
    }

    private fun rideOnDateValidate(date: Date): Boolean {
        //乗車日の過去日チェック
        val nowDate = Calendar.getInstance()
        nowDate.set(Calendar.HOUR_OF_DAY, 0)
        nowDate.set(Calendar.MINUTE, 0)
        nowDate.set(Calendar.SECOND, 0)
        nowDate.set(Calendar.MILLISECOND, 0)

        return if (date.before(nowDate.time)) {
            println("乗車日が過去日")
            false
        } else true
    }
}