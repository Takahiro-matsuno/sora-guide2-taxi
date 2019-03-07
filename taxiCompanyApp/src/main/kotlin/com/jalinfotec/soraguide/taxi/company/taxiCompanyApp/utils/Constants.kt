package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils

object Constants {
    // TODO 入力値の制限は？
    val PAX_RANGE = 0..20

    // TODO ステータスの更新のユースケース
    val reservationStatus = mutableMapOf(
            1 to "受付中",
            2 to "予約確定",
            3 to "変更受付中",
            4 to "キャンセル受付中",
            5 to "キャンセル済",
            6 to "完了")

    // 環境変数から取得する
    val FROM_ADDRESS = System.getenv("FROM_ADDRESS")

    enum class MAIL_TYPE { RESERVE, CANCEL, NONE }

    val mailSubject = mutableMapOf(
            MAIL_TYPE.RESERVE to "ご予約が確定しました",
            MAIL_TYPE.CANCEL to "取消完了のお知らせ"
    )

    val mailContent = mutableMapOf(
            MAIL_TYPE.RESERVE to "予約確定だよん",
            MAIL_TYPE.CANCEL to "予約は取り消したよん"
    )

}