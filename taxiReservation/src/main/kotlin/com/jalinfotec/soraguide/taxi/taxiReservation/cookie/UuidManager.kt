package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UuidManager {

    /**
     * 初回起動時にUUIDの有無を確認し、ない場合は作成する。
     */
    fun check(request: HttpServletRequest, response: HttpServletResponse) {
        val requestUuid = getUuid(request)
        if (requestUuid.isNullOrBlank()) {
            println("UUID発行")
            createUuid(request, response)
        } else {
            println("発行済みUUIDを使用")
        }
    }

    fun getUuid(request: HttpServletRequest): String? {
        return CookieManager().getFromCookie(request)
    }

    private fun createUuid(request: HttpServletRequest, response: HttpServletResponse) {
        val uuid = UUID.randomUUID().toString()
        println(uuid)
        CookieManager().setCookie(request, response, uuid)
    }
}