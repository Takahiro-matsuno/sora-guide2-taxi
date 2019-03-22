package com.jalinfotec.soraguide.taxi.taxiReservation.utils

import java.util.*

object Constants {
    //開発用オプション
    //TRUEの場合、Androidアプリ/WEBの判定が無効になり、WEBから実行していてもAndroidアプリでの処理動作が確認できる
    const val TEST_MODE = false

    val PAX_RANGE = 0..20
    val APP_LANG = Locale.JAPAN!!

    // 予約ステータス
    val reservationStatus = mutableMapOf(
            1 to "受付中",
            2 to "予約確定",
            3 to "変更受付中",
            4 to "キャンセル受付中",
            5 to "キャンセル済",
            6 to "完了")

    // 予約ステータス毎の予約変更/取消操作の可否
    val isChangeByStatus = mutableMapOf(
            1 to true,
            2 to true,
            3 to true,
            4 to false,
            5 to false,
            6 to false)

    // 予約ステータス毎の予約一覧/詳細表示の可否
    val isDetailByStatus = mutableMapOf(
            1 to true,
            2 to true,
            3 to true,
            4 to true,
            5 to false,
            6 to false)

    // 送信元メールアドレス設定、環境変数から取得する
    val FROM_ADDRESS = System.getenv("FROM_ADDRESS")

    // メール種別
    enum class MAIL_TYPE { RESERVE, CHANGE, CANCEL, NONE }

    // メール種別毎の件名設定
    val mailSubject = mutableMapOf(
            MAIL_TYPE.RESERVE to "ご予約を承りました",
            MAIL_TYPE.CHANGE to "ご予約の変更を承りました",
            MAIL_TYPE.CANCEL to "ご予約の取消を承りました"
    )

    // メール種別毎の本文テンプレート
    // %で囲まれた項目は動的項目で別途差し替え処理が必要
    val mailContent = mutableMapOf(
            MAIL_TYPE.RESERVE to
                    "%name% 様\n" +
                    "\n" +
                    "タクシーをご予約いただきありがとうございます。\n" +
                    "以下のご予約を承りました。\n" +
                    "\n" +
                    "■ご予約内容\n" +
                    "\n" +
                    "＜予約番号＞\n" +
                    "%rsvId%\n" +
                    "\n" +
                    "＜予約情報＞\n" +
                    "乗車日付：%rideOnDate%\n" +
                    "乗車時刻：%rideOnTime%\n" +
                    "\n" +
                    "\n" +
                    "■ご案内\n" +
                    "予約は確定ではありません。\n" +
                    "タクシー会社の確認後、改めてご予約確定のご連絡メールをお送り致します。\n" +
                    "\n" +
                    "ご予約の変更をご希望のお客様は下記URLから変更いただくか、タクシー会社へ直接ご連絡ください。\n" +
                    "%url%\n" +
                    "\n" +
                    "\n" +
                    "■お問い合わせ\n" +
                    "タクシー会社：%companyName%\n" +
                    "お電話：%companyContact%\n" +
                    "\n" +
                    "\n" +
                    "当メールは送信専用です。\n" +
                    "\n",

            MAIL_TYPE.CHANGE to
                    "%name% 様\n" +
                    "\n" +
                    "タクシーをご予約いただきありがとうございます。\n" +
                    "以下のご予約の変更を承りました。\n" +
                    "\n" +
                    "■ご予約内容\n" +
                    "\n" +
                    "＜予約番号＞\n" +
                    "%rsvId%\n" +
                    "\n" +
                    "＜予約情報＞\n" +
                    "乗車日付：%rideOnDate%\n" +
                    "乗車時刻：%rideOnTime%\n" +
                    "\n" +
                    "\n" +
                    "■ご案内\n" +
                    "変更は確定ではありません。\n" +
                    "タクシー会社の確認後、改めてご予約確定のご連絡メールをお送り致します。\n" +
                    "\n" +
                    "ご予約の変更をご希望のお客様は下記URLから変更いただくか、タクシー会社へ直接ご連絡ください。\n" +
                    "%url%\n" +
                    "\n" +
                    "\n" +
                    "■お問い合わせ\n" +
                    "タクシー会社：%companyName%\n" +
                    "お電話：%companyContact%\n" +
                    "\n" +
                    "\n" +
                    "当メールは送信専用です。\n" +
                    "\n",

            MAIL_TYPE.CANCEL to
                    "%name% 様\n" +
                    "\n" +
                    "タクシーをご予約いただきありがとうございます。\n" +
                    "以下のご予約のお取消しを承りました。\n" +
                    "タクシー会社の確認後、改めてご連絡メールをお送り致します。\n" +
                    "\n" +
                    "■ご予約内容\n" +
                    "\n" +
                    "＜予約番号＞\n" +
                    "%rsvId%\n" +
                    "\n" +
                    "＜予約情報＞\n" +
                    "乗車日付：%rideOnDate%\n" +
                    "乗車時刻：%rideOnTime%\n" +
                    "\n" +
                    "\n" +
                    "■ご案内\n" +
                    "ご予約の変更をご希望のお客様はタクシー会社へ直接ご連絡ください。\n" +
                    "\n" +
                    "\n" +
                    "■お問い合わせ\n" +
                    "タクシー会社：%companyName%\n" +
                    "お電話：%companyContact%\n" +
                    "\n" +
                    "\n" +
                    "当メールは送信専用です。\n" +
                    "\n"
    )
}