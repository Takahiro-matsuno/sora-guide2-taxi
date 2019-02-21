package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.CookieManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ListForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ReservationListService(
        private val reservationRepository: ReservationInfoRepository) {

    fun getList(request: HttpServletRequest, response: HttpServletResponse): MutableList<ListForm> {
        val cookieManager = CookieManager()

        //Cookieから予約番号を取得（CookieManager呼び出し）
        val rsvIdList = cookieManager.getFromCookie(request)

        //予約情報の取得
        val rsvInfoList = reservationRepository.findAllById(rsvIdList)
        val rsvList = mutableListOf<ListForm>()

        //表示対象の予約を確認
        val newRsvIdList = mutableListOf<String>()
        val iterator = rsvInfoList.iterator()
        while (iterator.hasNext()) {
            val book = iterator.next()
            //ステータスが4or5（キャンセル済みor完了）のものは弾く
            //乗車日が過去日のものは弾く
            when {
                book.status >= 4 -> {
                    println("予約番号${book.id}はステータスがキャンセル済みor完了のため表示対象外")
                    iterator.remove()
                }
                compareByDate(book.date) -> {
                    println("予約番号${book.id}は乗車日が過去日のため表示対象外")
                    iterator.remove()
                }
                else -> {
                    newRsvIdList.add(book.id)
                    if (convertRsvInfo2ListForm(book) != null) {
                        rsvList.add(convertRsvInfo2ListForm(book)!!)
                    }
                }
            }
        }

        //予約表示対象の予約番号をカンマ区切りでString変数に格納する
        val newBookingIdStr = newRsvIdList.joinToString("-")

        //Cookieの値を更新する
        cookieManager.setCookie(request, response, newBookingIdStr)

        //表示対象の予約リストを返す
        return rsvList
    }

    fun compareByDate(rsvDate: Date): Boolean {
        val nowDate = Calendar.getInstance()
        nowDate.set(Calendar.HOUR_OF_DAY, 0)
        nowDate.set(Calendar.MINUTE, 0)
        nowDate.set(Calendar.SECOND, 0)
        nowDate.set(Calendar.MILLISECOND, 0)

        return rsvDate.before(nowDate.time)
    }

    fun convertRsvInfo2ListForm(rsvInfo: ReservationInformation): ListForm? {

        // 予約ステータスの置き換え
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        return ListForm(
                rsvInfo.id,
                statusName,
                rsvInfo.date.toString(),
                rsvInfo.time.toString()
        )
    }
}