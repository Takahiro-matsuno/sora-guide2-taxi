package com.jalinfotec.soraguide.taxi.taxiReservation.utils

object Constants {
    const val TEST_MODE = false
    val PAX_RANGE = 0..20
    val reservationStatus = mutableMapOf(
            1 to "受付中",
            2 to "予約確定",
            3 to "変更受付中",
            4 to "配車中",
            5 to "キャンセル受付中",
            6 to "キャンセル済",
            7 to "完了")
}