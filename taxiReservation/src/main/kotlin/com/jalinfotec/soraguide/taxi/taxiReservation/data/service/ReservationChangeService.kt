package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.SessionManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.validation.ChangeValidation
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class ReservationChangeService(
        private val reservationRepository: ReservationInfoRepository,
        private val taxiRepository: TaxiInfoRepository
) {

    /**
     * 変更入力画面用のForm設定
     */
    fun getChangeDetail(id: String, request: HttpServletRequest): ChangeForm? {
        val rsvId = SessionManager().checkSession(id, request)

        println("【変更入力】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId)

        //予約変更可否チェック
        if (!ChangeValidation().checkChangePossible(rsvInfo)) {
            throw Exception()
        }

        //タクシー会社IDからタクシー会社名を取得
        val companyNameOptional = taxiRepository.findById(rsvInfo.companyId)

        // 予約ステータス文言の設定
        val statusName = Constants.reservationStatus[rsvInfo.status]


        return if (companyNameOptional.isPresent && statusName != null) {
            ChangeForm(
                    rsvInfo.reservationId,
                    statusName,
                    rsvInfo.rideOnDate,
                    rsvInfo.rideOnTime.toString().substring(0, 5),
                    rsvInfo.adult,
                    rsvInfo.child,
                    rsvInfo.carDispatchNumber,
                    companyNameOptional.get().companyName,
                    rsvInfo.destination,
                    rsvInfo.passengerName,
                    rsvInfo.passengerPhonetic,
                    rsvInfo.passengerContact,
                    rsvInfo.mail,
                    rsvInfo.mail,
                    rsvInfo.comment,
                    rsvInfo.carNumber,
                    rsvInfo.carContact,
                    rsvInfo.notice,
                    rsvInfo.lastUpdate
            )
        } else {
            println("【ERROR】タクシー会社情報、または予約ステータスの取得エラー")
            null
        }

    }

    /**
     * 予約変更処理
     */
    @Transactional
    fun change(changeInfo: ChangeForm, request: HttpServletRequest): String {
        val sessionManager = SessionManager()
        val rsvId = sessionManager.checkSession(changeInfo.id, request)

        println("【予約変更】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId)

        //予約変更可否チェック
        if (!ChangeValidation().checkChangePossible(rsvInfo)) {
            throw Exception()
        }

        //最終更新日の確認
        if (rsvInfo.lastUpdate != changeInfo.lastUpdate) {
            println("最終更新日アンマッチ")
            throw Exception()
        }

        //Time型の形式揃え
        if (changeInfo.rideOnTime.length == 5) {
            changeInfo.rideOnTime += ":00"
        }

        //FormからEntityに変更項目のみ詰め替え
        rsvInfo.rideOnDate = changeInfo.rideOnDate
        rsvInfo.rideOnTime = Time.valueOf(changeInfo.rideOnTime)
        rsvInfo.adult = changeInfo.adult
        rsvInfo.child = changeInfo.child
        rsvInfo.carDispatchNumber = changeInfo.carDispatchNumber
        rsvInfo.destination = changeInfo.destination
        rsvInfo.passengerName = changeInfo.passengerName
        rsvInfo.passengerPhonetic = changeInfo.passengerPhonetic
        rsvInfo.passengerContact = changeInfo.passengerContact
        rsvInfo.mail = changeInfo.mail
        rsvInfo.comment = changeInfo.comment
        rsvInfo.lastUpdate = Timestamp(System.currentTimeMillis())

        //ステータス設定
        if (rsvInfo.status == 2) {
            //ステータスが予約確定の場合、変更時にステータスを「変更受付中」に更新する
            rsvInfo.status = 3
        }

        //SQL呼び出し
        reservationRepository.save(rsvInfo)

        //セッションの予約番号を開放する
        sessionManager.deleteSession(request)

        return rsvInfo.reservationId
    }

    /**
     * 予約取消処理
     */
    @Transactional
    fun delete(id: String, lastUpdate: Timestamp, request: HttpServletRequest): String {
        val sessionManager = SessionManager()
        val rsvId = sessionManager.checkSession(id, request)
        println("【予約取消】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId)

        //予約変更可否チェック
        if (!ChangeValidation().checkChangePossible(rsvInfo)) {
            throw Exception()
        }

        //最終更新日の確認
        if (rsvInfo.lastUpdate != lastUpdate) {
            println("最終更新日アンマッチ")
            throw Exception()
        }

        //ステータスを更新
        if (rsvInfo.status == 1) {
            //ステータスが受付中の場合はタクシー会社の確認無しで「キャンセル済」とする
            rsvInfo.status = 5
        } else {
            //ステータスを「キャンセル受付中」とする
            rsvInfo.status = 4
        }

        //DBアクセス
        reservationRepository.save(rsvInfo)

        //セッションの予約番号を開放する
        sessionManager.deleteSession(request)

        return rsvInfo.reservationId
    }

    /**
     * 予約変更、取消フロー用の予約情報取得処理
     *
     * セッションに保持している予約番号を用いて予約検索を行う
     * 予約情報が存在しない場合はエラーを投げる。
     */
    @Transactional
    fun getRsvInfo(id: String): ReservationInformation {
        val rsvInfoOptional = reservationRepository.findById(id)

        //予約が存在しない場合、エラー
        if (!rsvInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }

        return rsvInfoOptional.get()
    }
}