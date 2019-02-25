package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Cookie管理クラス
 * スマホアプリから実行している場合のみ、Cookieのやり取りを行う
 */
class CookieManager {

    fun setCookie(request: HttpServletRequest, response: HttpServletResponse, uuid: String) {
        val newCookie = Cookie("uuid", uuid)
        newCookie.maxAge = 10 * 365 * 24 * 60 * 60
        newCookie.path = "/"
        if ("https" == request.scheme) {
            newCookie.secure = true
        }
        response.addCookie(newCookie)

    }

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
}