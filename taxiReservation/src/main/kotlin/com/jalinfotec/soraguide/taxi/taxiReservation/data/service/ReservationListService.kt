package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.CookieManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ReservationListService(
        private val reservationRepository: ReservationInfoRepository) {

    fun getList(request: HttpServletRequest, response: HttpServletResponse): MutableList<ReservationInformation> {
        val cookieManager = CookieManager()

        //Cookieから予約番号を取得（CookieManager呼び出し）
        val bookingIdList = cookieManager.getFromCookie(request)

        //予約番号をSQL投げ
        val bookingList = reservationRepository.findAllById(bookingIdList)

        //表示対象の予約を確認
        val newBookingIdList = mutableListOf<String>()
        for (book in bookingList) {
            //ステータスが4or5（キャンセル済みor完了）のものは弾く
            //乗車日が過去日のものは弾く
            if (book.status >= 4) {
                println("予約番号${book.id}はステータスがキャンセル済みor完了のため表示対象外")
                bookingList.remove(book)
            } else if (book.date < Calendar.getInstance().time) {
                println("予約番号${book.id}は乗車日が過去日のため表示対象外")
                bookingList.remove(book)
            } else {
                newBookingIdList.add(book.id)
            }
        }

        //予約表示対象の予約番号をカンマ区切りでString変数に格納する
        val newBookingIdStr = newBookingIdList.joinToString("-")

        //Cookieの値を更新する
        cookieManager.setCookie(request, response, newBookingIdStr)

        //表示対象の予約リストを返す
        return bookingList
    }
}