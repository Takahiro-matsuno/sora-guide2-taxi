package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.CookieManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ListForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.sql.Date
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ReservationListService(
        private val reservationRepository: ReservationInfoRepository) {

    @Transactional
    fun getList(request: HttpServletRequest, response: HttpServletResponse): MutableList<ListForm> {
        val cookieManager = CookieManager()

        //CookieからUUIDを取得（CookieManager呼び出し）
        val uuid = cookieManager.getFromCookie(request) ?: throw Exception()

        //予約情報の取得
        val rsvInfoList =
                reservationRepository.findByUuidAndStatusLessThanAndRideOnDateGreaterThanEqualOrderByRideOnDateAscRideOnTimeAsc(
                        uuid, 6, Date(System.currentTimeMillis()))
        val rsvList = mutableListOf<ListForm>()

        //予約情報のForm変換
        val iterator = rsvInfoList.iterator()
        while (iterator.hasNext()) {
            val book = iterator.next()
            if (convertRsvInfo2ListForm(book) != null) {
                rsvList.add(convertRsvInfo2ListForm(book)!!)
            }
        }

        //表示対象の予約リストを返す
        return rsvList
    }

    fun convertRsvInfo2ListForm(rsvInfo: ReservationInformation): ListForm? {

        // 予約ステータスの置き換え
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        return ListForm(
                rsvInfo.reservationId,
                statusName,
                rsvInfo.rideOnDate.toString(),
                rsvInfo.rideOnTime.toString()
        )
    }
}