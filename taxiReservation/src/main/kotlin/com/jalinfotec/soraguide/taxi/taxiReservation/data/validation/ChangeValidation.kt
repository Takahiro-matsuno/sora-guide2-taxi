package com.jalinfotec.soraguide.taxi.taxiReservation.data.validation

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import java.util.*

class ChangeValidation {

    /**
     * 予約変更可否チェック
     */
    fun checkChangePossible(rsvInfo: ReservationInformation): Boolean {
        //ステータスによるチェック
        if (Constants.isChangeByStatus[rsvInfo.status] != true) {
            println("【ERROR】変更不可のステータス")
            return false
        }

        //乗車時間によるステータスチェック
        //今日の日付を取得
        val nowDate = Calendar.getInstance()
        println("現在時刻:${nowDate.time}")

        //乗車日時を取得
        val rsvDate = Calendar.getInstance()
        rsvDate.time = rsvInfo.rideOnDate
        rsvDate.set(Calendar.HOUR_OF_DAY, rsvInfo.rideOnTime.toLocalTime().hour)
        rsvDate.set(Calendar.MINUTE, rsvInfo.rideOnTime.toLocalTime().minute)
        println("乗車日時:${rsvDate.time}")

        if (rsvDate.before(nowDate)) {
            println("【ERROR】乗車時間を過ぎている")
            return false
        }

        return true
    }
}