package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UuidManager {
    companion object {
        //開発中はUSER-AGENTの判定を切りたい
        //TEST_MODEがtrueの時はUSER-AGENT判定無し
        const val TEST_MODE = false
    }

    /**
     * 初回起動時にUUIDの有無を確認し、ない場合は作成する。
     */
    fun check(request: HttpServletRequest, response: HttpServletResponse) {
        //UserAgentでアプリのWebViewから開いているかどうか判定
        val userAgent = request.getHeader("user-agent")
        if (TEST_MODE || userAgent.indexOf("sora-GuideApp") > 0) {
            //UUID発行済みかどうか確認
            val requestUuid = getUuid(request)
            if (requestUuid.isNullOrBlank()) {
                println("UUID発行")
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
        return CookieManager().getFromCookie(request)
    }

    /**
     * UUIDをCookieに設定
     */
    fun setUuid(request: HttpServletRequest, response: HttpServletResponse, uuid: String):String {
        println("【UUID設定】START")

        //すでに予約情報にUUIDが設定されている場合は更新しない
        val newUuid =
                if (uuid.isBlank()) {
                    println("UUID新規設定")
                    UUID.randomUUID().toString()
                } else uuid

        //UUIDをCookieに設定
        CookieManager().setCookie(request, response, newUuid)
        return newUuid
    }

    /**
     * UUID発行
     */
    private fun createUuid(request: HttpServletRequest, response: HttpServletResponse) {
        //UUIDを新規発行
        val uuid = UUID.randomUUID().toString()
        println(uuid)

        //UUIDをCookieに設定
        CookieManager().setCookie(request, response, uuid)
    }
}