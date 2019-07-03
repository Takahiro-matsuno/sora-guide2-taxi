package com.jalinfotec.soraguide.taxi.taxiReservation.domain.service

import com.jalinfotec.soraguide.taxi.taxiReservation.utils.cookie.UuidManager
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.repository.NumberingRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.hibernate.exception.JDBCConnectionException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Time
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest

@Service
class ReservationCompleteService(
        private val reservationRepository: ReservationInfoRepository,
        private val numberingRepository: NumberingRepository,
        private val taxiInfoService: TaxiInformationService,
        private val sendMailService: SendMailService) {

    /**
     * 予約完了処理
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun complete(input: ReservationForm, request: HttpServletRequest): String {
        // タクシー会社情報の取得
        val taxiInfo = taxiInfoService.getTaxiInfoFromCompanyName(input.companyName)

        // 予約情報のEntity詰め替え
        val rsvInfo = convertRsvForm2RsvInfo(input, taxiInfo, request)
        println("【予約完了】予約ID：${rsvInfo.reservationId}")

        // 予約登録処理
        insertRsvInfo(rsvInfo)

        // メール送信処理
        if (sendMailService.sendMail(rsvInfo, taxiInfo, Constants.MAIL_TYPE.RESERVE)) {
            println("メール送信完了")
        } else {
            println("メール送信失敗")
        }

        return rsvInfo.reservationId
    }

    /**
     * 予約フォームの内容をエンティティに詰め替え
     */
    private fun convertRsvForm2RsvInfo(input: ReservationForm, taxiInfo: TaxiInformation,
                                       request: HttpServletRequest): ReservationInformation {

        // UUIDが存在する場合は取得、存在しない場合はブランク
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
                companyId = taxiInfo.id,
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
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun setId(): String {
        // 新規の予約番号を取得
        val numbering = numberingRepository.findById("reservation_info").get()

        // 予約番号を10桁にゼロパディング
        val result = String.format("%010d", numbering.nextValue)

        // 予約番号の値を更新
        numbering.nextValue++
        numberingRepository.save(numbering)

        return result
    }

    /**
     * 予約登録処理
     */
    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun insertRsvInfo(rsvInfo: ReservationInformation) {
        reservationRepository.save(rsvInfo)
    }
}