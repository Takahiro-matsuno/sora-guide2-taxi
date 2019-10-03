package com.jalinfotec.soraguide.taxi.taxiReservation.domain.service

import com.jalinfotec.soraguide.taxi.taxiReservation.utils.cookie.SessionManager
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.validation.ChangeValidation
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Time
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest

@Service
class ReservationChangeService(
        private val reservationRepository: ReservationInfoRepository,
        private val taxiInfoService: TaxiInformationService,
        private val sendMailService: SendMailService
) {

    /**
     * 変更入力画面用の予約情報取得
     */
    fun getChangeDetail(id: String, request: HttpServletRequest): ChangeForm? {
        // セッションの予約番号と画面から送られた番号の比較
        val rsvId = SessionManager().checkSession(id, request)
        println("【変更入力】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId) ?: return null

        //予約変更可否チェック
        if (!ChangeValidation().checkChangePossible(rsvInfo)) {
            return null
        }

        //タクシー会社IDからタクシー会社情報を取得
        val companyName = taxiInfoService.getCompanyName(rsvInfo.companyId)

        // 予約ステータス文言の設定
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        //Form詰め替え
        return ChangeForm(
                rsvInfo.reservationId,
                statusName,
                rsvInfo.rideOnDate,
                rsvInfo.rideOnTime.toString().substring(0, 5),
                rsvInfo.adult,
                rsvInfo.child,
                rsvInfo.carDispatchNumber,
                companyName,
                rsvInfo.destination,
                rsvInfo.passengerName,
                //rsvInfo.passengerPhonetic,
                rsvInfo.passengerContact,
                rsvInfo.mail,
                rsvInfo.mail,
                rsvInfo.comment,
                rsvInfo.carNumber,
                rsvInfo.carContact,
                rsvInfo.notice,
                rsvInfo.lastUpdate
        )
    }

    /**
     * 予約変更処理
     */
    fun change(changeInfo: ChangeForm, request: HttpServletRequest): String? {
        // セッションの予約番号と画面から送られた番号の比較
        val sessionManager = SessionManager()
        val rsvId = sessionManager.checkSession(changeInfo.id, request)
        println("【予約変更】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId) ?: return null

        //予約変更可否チェック
        if (!ChangeValidation().checkChangePossible(rsvInfo)) {
            return null
        }

        //最終更新日の確認
        if (rsvInfo.lastUpdate != changeInfo.lastUpdate) {
            println("最終更新日アンマッチ")
            return null
        }

        //Time型の形式揃え
        if (changeInfo.rideOnTime.length == 5) {
            changeInfo.rideOnTime += ":00"
        }

        //FormからEntityに詰め替え
        rsvInfo.rideOnDate = changeInfo.rideOnDate
        rsvInfo.rideOnTime = Time.valueOf(changeInfo.rideOnTime)
        rsvInfo.adult = changeInfo.adult
        rsvInfo.child = changeInfo.child
        rsvInfo.carDispatchNumber = changeInfo.carDispatchNumber
        rsvInfo.destination = changeInfo.destination
        rsvInfo.passengerName = changeInfo.passengerName
        //rsvInfo.passengerPhonetic = changeInfo.passengerPhonetic
        rsvInfo.passengerContact = changeInfo.passengerContact
        rsvInfo.mail = changeInfo.mail
        rsvInfo.comment = changeInfo.comment
        rsvInfo.lastUpdate = Timestamp(System.currentTimeMillis())

        //ステータス設定
        if (rsvInfo.status == 2) {
            //ステータスが予約確定の場合、変更時にステータスを「変更受付中」に更新する
            rsvInfo.status = 3
        }

        //予約更新
        updateRsvInfo(rsvInfo)

        //セッションの予約番号を開放する
        sessionManager.deleteSession(request)

        // メール送信処理
        val taxiInfo = taxiInfoService.getTaxiInfo(rsvInfo.companyId)
        if (taxiInfo != null) {
            if (sendMailService.sendMail(rsvInfo, taxiInfo, Constants.MAIL_TYPE.CHANGE)) {
                println("メール送信完了")
            } else {
                println("メール送信失敗")
            }
        }

        return rsvInfo.reservationId
    }

    /**
     * 予約取消処理
     */
    fun delete(id: String, lastUpdate: Timestamp, request: HttpServletRequest): String? {
        // セッションの予約番号と画面から送られた番号の比較
        val sessionManager = SessionManager()
        val rsvId = sessionManager.checkSession(id, request)
        println("【予約取消】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId) ?: return null

        //予約変更可否チェック
        if (!ChangeValidation().checkChangePossible(rsvInfo)) {
            return null
        }

        //最終更新日の確認
        if (rsvInfo.lastUpdate != lastUpdate) {
            println("最終更新日アンマッチ")
            return null
        }

        //ステータスを更新
        if (rsvInfo.status == 1) {
            //ステータスが受付中の場合はタクシー会社の確認無しで「キャンセル済」とする
            rsvInfo.status = 5
        } else {
            //ステータスを「キャンセル受付中」とする
            rsvInfo.status = 4
        }

        //予約更新
        updateRsvInfo(rsvInfo)

        //セッションの予約番号を開放する
        sessionManager.deleteSession(request)

        // メール送信処理
        val taxiInfo = taxiInfoService.getTaxiInfo(rsvInfo.companyId)
        if (taxiInfo != null) {
            if (sendMailService.sendMail(rsvInfo, taxiInfo, Constants.MAIL_TYPE.CANCEL)) {
                println("メール送信完了")
            } else {
                println("メール送信失敗")
            }
        }

        return rsvInfo.reservationId
    }

    /**
     * 予約変更、取消フロー用の予約情報取得処理
     *
     * 予約番号を用いて予約検索を行う
     * 予約情報が存在しない場合はエラーを投げる。
     */
    @Transactional(readOnly = true)
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getRsvInfo(id: String): ReservationInformation? {
        //予約情報取得
        val rsvInfoOptional = reservationRepository.findById(id)

        //予約が存在しない場合、エラー
        if (!rsvInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            return null
        }

        return rsvInfoOptional.get()
    }

    /**
     * 予約更新処理
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun updateRsvInfo(rsvInfo: ReservationInformation) {
        reservationRepository.save(rsvInfo)
    }
}