package com.jalinfotec.soraguide.taxi.taxiReservation.utils

object Constants {
    const val TEST_MODE = true
    val PAX_RANGE = 0..20
    val reservationStatus = mutableMapOf(
            1 to "受付中",
            2 to "予約確定",
            3 to "変更受付中",
            4 to "キャンセル受付中",
            5 to "キャンセル済",
            6 to "完了")

    val isChangeByStatus = mutableMapOf(
            1 to true,
            2 to true,
            3 to true,
            4 to false,
            5 to false,
            6 to false)

    val isDetailByStatus = mutableMapOf(
            1 to true,
            2 to true,
            3 to true,
            4 to true,
            5 to false,
            6 to false)
}