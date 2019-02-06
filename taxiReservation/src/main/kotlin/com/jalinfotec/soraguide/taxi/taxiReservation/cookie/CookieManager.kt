package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieManager {
    fun setCookie(request: HttpServletRequest, response: HttpServletResponse, rsvId: String) {
        val newCookie = Cookie("rsvId", rsvId)
        newCookie.maxAge = 365 * 24 * 60 * 60
        newCookie.path = "/"
        if ("https" == request.scheme) {
            newCookie.secure = true
        }
        response.addCookie(newCookie)
    }

    fun getFromCookie(request: HttpServletRequest):List<String> {
        val cookies = request.cookies
        var bookingId: String? = null

        if (cookies != null) {
            for (cookie in cookies) {
                if ("rsvId" == cookie.name) {
                    bookingId = cookie.value
                }
            }
        }

        return bookingId?.split("-") ?: listOf()
    }
}