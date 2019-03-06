package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.SessionManager
import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.UuidManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.DetailForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.validation.ChangeValidation
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ReservationDetailService(
        private val reservationRepository: ReservationInfoRepository,
        private val taxiRepository: TaxiInfoRepository
) {

    /**
     * 【予約アプリ】予約詳細取得
     */
    @Transactional
    fun getDetail(id: String, request: HttpServletRequest): DetailForm? {
        println("【予約情報取得】予約ID：$id")

        val uuid = UuidManager().getUuid(request) ?: throw Exception()

        //DBから引数のIDとマッチする予約情報を取得
        val rsvInfoOptional = reservationRepository.findByReservationIdAndUuid(id, uuid)

        return if (rsvInfoOptional.isPresent) {
            //セッションに予約番号を設定
            setRsvId2Session(rsvInfoOptional.get().reservationId, request)
            convertRsvInfo2RsvForm(rsvInfoOptional.get())
        } else null
    }

    /**
     * 【予約サイト】予約認証
     */
    @Transactional
    fun detailCertificates(id: String, mail: String, request: HttpServletRequest, response: HttpServletResponse): DetailForm? {
        println("【予約認証】予約番号：$id")

        //DBから引数のIDとマッチする予約情報を取得
        //TODO DB接続エラー
        val rsvInfoOptional = reservationRepository.findById(id)

        if (rsvInfoOptional.isPresent) {
            //メールアドレス一致チェック
            if (rsvInfoOptional.get().mail.trim() != mail) {
                //TODO 認証エラー
                println("【ERROR】メールアドレス不一致")
                return null
            }
        } else {
            //TODO 予約照会エラー
            println("【ERROR】予約情報が取得できない")
            return null
        }

        //セッションに予約番号を設定
        setRsvId2Session(rsvInfoOptional.get().reservationId, request)

        return convertRsvInfo2RsvForm(rsvInfoOptional.get())
    }

    /**
     * セッションマネージャを呼び出す
     * 予約番号をセッションに保持する処理
     */
    fun setRsvId2Session(rsvId: String, request: HttpServletRequest) {
        val sessionManager = SessionManager()
        sessionManager.setSession(rsvId, request)
    }

    /**
     * 予約情報Entityを予約情報フォームへ詰め替え
     */
    @Transactional
    fun convertRsvInfo2RsvForm(rsvInfo: ReservationInformation): DetailForm? {
        //タクシー会社IDからタクシー会社名を取得
        //TODO DB接続エラー
        val companyNameOptional = taxiRepository.findById(rsvInfo.companyId)

        // 予約ステータス文言の設定
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        // 乗車日時の表示形式を変更
        val rideOnDateStr = rsvInfo.rideOnDate.toString().replace("-", "/")
        val rideOnTimeStr = rsvInfo.rideOnTime.toString().substring(0, 5)

        //予約変更可否チェック
        val isChange = ChangeValidation().checkChangePossible(rsvInfo)

        val companyContact = companyNameOptional.get().contact

        return if (companyNameOptional.isPresent) {
            DetailForm(
                    rsvInfo.reservationId,
                    statusName,
                    rideOnDateStr,
                    rideOnTimeStr,
                    rsvInfo.adult.toString(),
                    rsvInfo.child.toString(),
                    rsvInfo.carDispatchNumber.toString(),
                    companyNameOptional.get().companyName,
                    rsvInfo.destination,
                    rsvInfo.passengerName,
                    rsvInfo.passengerPhonetic,
                    rsvInfo.passengerContact,
                    rsvInfo.mail,
                    rsvInfo.comment,
                    rsvInfo.carNumber,
                    rsvInfo.carContact,
                    rsvInfo.notice,
                    rsvInfo.lastUpdate,
                    isChange,
                    companyContact
            )
        } else {
            println("【ERROR】タクシー会社情報、または予約ステータスの取得エラー")
            null
        }
    }

    /**
     * 各種完了画面表示用コンテンツ取得
     */
    @Transactional
    fun getDetailForActionComplete(id: String): MutableMap<String, String> {
        println("【予約情報取得】予約ID：$id")

        //DBから引数のIDとマッチする予約情報を取得
        val rsvInfoOptional = reservationRepository.findById(id)
        val taxiInfoOptional = taxiRepository.findById(rsvInfoOptional.get().companyId)
        val statusName = Constants.reservationStatus[rsvInfoOptional.get().status]

        if (!rsvInfoOptional.isPresent || statusName.isNullOrEmpty()) {
            throw Exception()
        }
        return mutableMapOf("reservationId" to rsvInfoOptional.get().reservationId,
                "reservationStatus" to statusName!!,
                "passengerName" to rsvInfoOptional.get().passengerName,
                "companyContact" to taxiInfoOptional.get().contact)
    }
}