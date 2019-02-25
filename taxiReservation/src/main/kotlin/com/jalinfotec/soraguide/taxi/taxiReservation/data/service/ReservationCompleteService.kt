package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.UuidManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.NumberingRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Time
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest

@Service
class ReservationCompleteService(
        private val reservationRepository: ReservationInfoRepository,
        private val numberingRepository: NumberingRepository,
        private val taxiInfoService: TaxiInformationService) {

    @Transactional(readOnly = false)
    fun complete(input: ReservationForm, request: HttpServletRequest): String {
        val rsvInfo = setReservation(input, request)
        println("【予約完了】予約ID：${rsvInfo.reservationId}")
        reservationRepository.save(rsvInfo)
        //cookieUpdate(bookingInfo, request, response)
        return rsvInfo.reservationId
    }

    private fun setReservation(input: ReservationForm, request: HttpServletRequest): ReservationInformation {
        //選択した会社名から会社IDを検索
        val taxiCompanyId = taxiInfoService.getCompanyId(input.company_name)
        val uuid = UuidManager().getUuid(request) ?: ""

        return ReservationInformation(
                reservationId = setId(),

                //予約時のステータスは1で固定
                status = 1,

                //入力データをセット
                rideOnDate = input.date,
                rideOnTime = Time.valueOf(input.time + ":00"),
                adult = input.adult,
                child = input.child,
                carDispatchNumber = input.car_dispatch,
                companyId = taxiCompanyId,
                destination = input.destination,
                passengerName = input.name,
                passengerPhonetic = input.phonetic,
                passengerContact = input.phone,
                mail = input.mail,
                comment = input.comment,

                //タクシー会社入力欄はブランク
                carNumber = "",
                carContact = "",
                notice = "",

                uuid = uuid,
                lastUpdate = Timestamp(System.currentTimeMillis())
        )
    }

    @Transactional(readOnly = false)
    fun setId(): String {
        val numbering = numberingRepository.findById("booking_info").get()
        val result = String.format("%010d", numbering.nextValue)

        numbering.nextValue++
        numberingRepository.save(numbering)

        return result
    }

    /*
    fun cookieUpdate(inputReservationInfo: ReservationInformation, request: HttpServletRequest, response: HttpServletResponse) {
        val cookieManager = CookieManager()

        //以下条件の予約情報を取得する
        //1.予約時の電話番号と一致する
        //2.予約時のメールアドレスと一致する
        //3.ステータスがキャンセル待ち(4)、または完了(5)ではない
        //4.乗車日が過去日でない
        val bookingInfoList =
                reservationRepository.findByPhoneAndMailAndStatusLessThanAndDateGreaterThanEqualOrderByIdAsc(
                        inputReservationInfo.passengerContact, inputReservationInfo.mail, 4, Date(Calendar.getInstance().timeInMillis)
                )

        //取得した予約情報から予約番号をカンマ区切りでString変数に格納する
        val idList = mutableListOf<String>()
        for (book in bookingInfoList) {
            println("【Cookie更新】予約ID：${book.reservationId}")
            idList.add(book.reservationId)
        }
        val idStr = idList.joinToString("-")

        //Cookieの値を更新する（名前は"rsvId"）
        cookieManager.setCookie(request, response, idStr)
    }*/
}