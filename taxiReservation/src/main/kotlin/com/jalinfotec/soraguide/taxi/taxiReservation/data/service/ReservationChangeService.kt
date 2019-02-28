package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.SessionManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.stereotype.Service
import java.sql.Time
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest

@Service
class ReservationChangeService(
        private val reservationRepository: ReservationInfoRepository
) {

    /**
     * 変更入力画面用のForm設定
     */
    fun getChangeDetail(id: String, request: HttpServletRequest): ChangeForm {
        val rsvId = SessionManager().checkSession(id, request)

        println("【変更入力】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId)

        //予約変更可否チェック
        if (!checkStatus(rsvInfo.status)) {
            throw Exception()
        }

        return ChangeForm(
                id = rsvInfo.reservationId,
                date = rsvInfo.rideOnDate,
                time = rsvInfo.rideOnTime.toString().substring(0, 5),
                adult = rsvInfo.adult,
                child = rsvInfo.child,
                car_dispatch = rsvInfo.carDispatchNumber,
                destination = rsvInfo.destination.trim(),
                phone = rsvInfo.passengerContact.trim(),
                comment = rsvInfo.comment.trim(),
                lastUpdate = rsvInfo.lastUpdate
        )
    }

    /**
     * 予約変更処理
     */
    fun change(changeInfo: ChangeForm, request: HttpServletRequest): String {
        val sessionManager = SessionManager()
        val rsvId = sessionManager.checkSession(changeInfo.id, request)

        println("【予約変更】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId)

        //予約変更可否チェック
        if (!checkStatus(rsvInfo.status)) {
            throw Exception()
        }

        //最終更新日の確認
        if (rsvInfo.lastUpdate != changeInfo.lastUpdate) {
            println("最終更新日アンマッチ")
            throw Exception()
        }

        //Time型の形式揃え
        if (changeInfo.time.length == 5) {
            changeInfo.time += ":00"
        }

        //FormからEntityに変更項目のみ詰め替え
        rsvInfo.rideOnDate = changeInfo.date
        rsvInfo.rideOnTime = Time.valueOf(changeInfo.time)
        rsvInfo.adult = changeInfo.adult
        rsvInfo.child = changeInfo.child
        rsvInfo.carDispatchNumber = changeInfo.car_dispatch
        rsvInfo.destination = changeInfo.destination
        rsvInfo.passengerContact = changeInfo.phone
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
    fun delete(id: String, lastUpdate: Timestamp, request: HttpServletRequest): String {
        val sessionManager = SessionManager()
        val rsvId = sessionManager.checkSession(id, request)
        println("【予約取消】予約番号：$rsvId")

        //予約情報取得
        val rsvInfo = getRsvInfo(rsvId)

        //予約変更可否チェック
        if (!checkStatus(rsvInfo.status)) {
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
            rsvInfo.status = 6
        } else {
            //ステータスを「キャンセル受付中」とする
            rsvInfo.status = 5
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
    fun getRsvInfo(id: String): ReservationInformation {
        val rsvInfoOptional = reservationRepository.findById(id)

        //予約が存在しない場合、エラー
        if (!rsvInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }

        return rsvInfoOptional.get()
    }

    /**
     * 予約変更可否チェック
     */
    fun checkStatus(status: Int): Boolean {
        if (status >= 4) {
            println("ERROR:変更不可のステータス")
            return false
        }
        return true
    }
}