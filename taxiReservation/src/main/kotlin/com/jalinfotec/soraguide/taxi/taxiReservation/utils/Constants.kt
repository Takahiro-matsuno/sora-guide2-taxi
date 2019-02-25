package com.jalinfotec.soraguide.taxi.taxiReservation.utils

object Constants {
    val PAX_RANGE = 0..20
    val reservationStatus = mutableMapOf(
            1 to "受付中",
            2 to "予約確定",
            3 to "配車中",
            4 to "キャンセル済み",
            5 to "完了")
}