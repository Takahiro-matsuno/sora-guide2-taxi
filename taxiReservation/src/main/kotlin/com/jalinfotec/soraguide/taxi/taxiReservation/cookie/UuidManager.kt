package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UuidManager {
    /**
     * 初回起動時にUUIDの有無を確認し、ない場合は作成する。
     */
    fun check(request: HttpServletRequest, response: HttpServletResponse) {
        //UserAgentでアプリのWebViewから開いているかどうか判定
        if (UserAgentManager().checkAndroidApp(request)) {
            //UUID発行済みかどうか確認
            val requestUuid = getUuid(request)
            if (requestUuid.isNullOrBlank()) {
                createUuid(request, response)
            } else {
                println("発行済みUUIDを使用")
            }
        } else {
            println("Web遷移のためUUIDは発行しない")
        }
    }

    /**
     * Cookieに設定されているUUIDの取得
     */
    fun getUuid(request: HttpServletRequest): String? {
        //UserAgentでアプリのWebViewから開いているかどうか判定
        if (UserAgentManager().checkAndroidApp(request)) {
            return CookieManager().getFromCookie(request)
        }
        return null
    }

    /**
     * UUID発行
     */
    private fun createUuid(request: HttpServletRequest, response: HttpServletResponse) {
        println("UUID発行")

        //UUIDを新規発行
        val uuid = UUID.randomUUID().toString()

        //UUIDをCookieに設定
        CookieManager().setCookie(request, response, uuid)
    }
}