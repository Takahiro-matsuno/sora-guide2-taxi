package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Cookie管理クラス
 */
class CookieManager {

    /**
     * Cookie設定クラス
     *
     * CookieにUUIDをセットする
     */
    fun setCookie(request: HttpServletRequest, response: HttpServletResponse, uuid: String) {
        val newCookie = Cookie("uuid", uuid)
        newCookie.maxAge = 10 * 365 * 24 * 60 * 60
        newCookie.path = "/"
        if ("https" == request.scheme) {
            newCookie.secure = true
        }
        response.addCookie(newCookie)

    }

    /**
     * Cookie取得クラス
     *
     * CookieからUUIDを取得する
     */
    fun getFromCookie(request: HttpServletRequest): String? {
        var uuid: String? = null

        val cookies = request.cookies

        if (cookies != null) {
            for (cookie in cookies) {
                //Cookieから"uuid"で保存している値を検索
                if ("uuid" == cookie.name) {
                    uuid = cookie.value
                    break
                }
            }
        }
        return uuid
    }

    /**
     * 設定言語取得クラス
     */
    fun getLocale(request: HttpServletRequest): String {
        val locale = request.locale.language

        // 設定言語が日本語、英語ならそのまま返す。それ以外の場合は日本語を返す。
        return when (locale) {
            "ja" -> ""
            "en" -> "$locale/"
            else -> ""
        }
    }
}