package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieManager(private val bookingRepository: BookingInfoRepository) {
    fun setCookie(request: HttpServletRequest, response: HttpServletResponse, rsvId: String) {
        val newCookie = Cookie("rsvId", rsvId)
        newCookie.maxAge = 365 * 24 * 60 * 60
        newCookie.path = "/"
        if ("https" == request.scheme) {
            newCookie.secure = true
        }
        response.addCookie(newCookie)
    }

    fun getCookie(request: HttpServletRequest) {
        val cookies = request.cookies
        var bookingId: String? = null
        val bookingIdList: MutableList<String> = mutableListOf()
        if (cookies != null) {
            for (cookie in cookies) {
                if ("rsvId" == cookie.name) {
                    bookingId = cookie.value
                }
            }
        }

        if(bookingId==null) {
            //終わり
        }else{
            //TODO bookingIDにはカンマ区切りで予約番号が入るような想定。切り分けてリスト化
            bookingIdList.add(bookingId)
        }
        val rsvList = bookingRepository.findAllById(bookingIdList)
        for (rsvInfo in rsvList) {

            if (rsvInfo.status == 5) {

            }
        }
    }
}