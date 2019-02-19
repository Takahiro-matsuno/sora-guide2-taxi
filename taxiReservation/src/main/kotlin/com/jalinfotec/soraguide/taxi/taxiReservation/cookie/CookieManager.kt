package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Cookie管理クラス
 * スマホアプリから実行している場合のみ、Cookieのやり取りを行う
 */
class CookieManager {
    fun setCookie(request: HttpServletRequest, response: HttpServletResponse, rsvId: String) {
        //UserAgentでアプリのWebViewから開いているかどうか判定
        //val userAgent = request.getHeader("user-agent")
      //  if (userAgent.indexOf("sora-GuideApp") > 0) {
            val newCookie = Cookie("rsvId", rsvId)
            newCookie.maxAge = 10 * 365 * 24 * 60 * 60
            newCookie.path = "/"
            if ("https" == request.scheme) {
                newCookie.secure = true
            }
            response.addCookie(newCookie)
        //}
    }

    fun getFromCookie(request: HttpServletRequest): List<String> {
        //UserAgentでアプリのWebViewから開いているかどうか判定
        val userAgent = request.getHeader("user-agent")
        var bookingId: String? = null

        //if (userAgent.indexOf("sora-GuideApp") > 0) {
            val cookies = request.cookies

            if (cookies != null) {
                for (cookie in cookies) {
                    //Cookieから"rsvId"で保存している値を検索
                    if ("rsvId" == cookie.name) {
                        bookingId = cookie.value
                    }
                }
            }
        //}

        //予約番号はハイフン区切りで格納されているので分割してリスト化
        return bookingId?.split("-") ?: listOf()
    }
}