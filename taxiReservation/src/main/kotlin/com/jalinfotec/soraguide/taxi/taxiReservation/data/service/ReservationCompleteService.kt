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

    /**
     * 予約完了処理
     */
    @Transactional
    fun complete(input: ReservationForm, request: HttpServletRequest): String {
        val rsvInfo = convertRsvForm2RsvInfo(input, request)
        println("【予約完了】予約ID：${rsvInfo.reservationId}")

        //TODO DB接続エラー
        reservationRepository.save(rsvInfo)

        return rsvInfo.reservationId
    }

    /**
     * 予約フォームの内容をエンティティに詰め替え
     */
    @Transactional(readOnly = true)
    private fun convertRsvForm2RsvInfo(input: ReservationForm, request: HttpServletRequest): ReservationInformation {
        //選択した会社名から会社IDを検索
        val taxiCompanyId = taxiInfoService.getCompanyId(input.companyName)
        val uuid = UuidManager().getUuid(request) ?: ""

        return ReservationInformation(
                reservationId = setId(),

                //予約時のステータスは1で固定
                status = 1,

                //入力データをセット
                rideOnDate = input.rideOnDate,
                rideOnTime = Time.valueOf(input.rideOnTime + ":00"),
                adult = input.adult,
                child = input.child,
                carDispatchNumber = input.carDispatchNumber,
                companyId = taxiCompanyId,
                destination = input.destination,
                passengerName = input.passengerName,
                passengerPhonetic = input.passengerPhonetic,
                passengerContact = input.passengerContact,
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

    /**
     * 予約番号の設定
     */
    @Transactional
    fun setId(): String {
        //TODO DB接続エラー
        val numbering = numberingRepository.findById("booking_info").get()
        val result = String.format("%010d", numbering.nextValue)

        numbering.nextValue++
        //TODO DB接続エラー
        numberingRepository.save(numbering)

        return result
    }
}