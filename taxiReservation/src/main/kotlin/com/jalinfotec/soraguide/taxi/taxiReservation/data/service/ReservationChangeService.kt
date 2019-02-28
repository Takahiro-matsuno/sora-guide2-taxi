package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

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
        println("【変更入力】予約番号：$id")

        //予約情報取得
        val rsvInfo = getRsvInfo(id)

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
        println("【予約変更】予約番号：${changeInfo.id}")

        //予約情報取得
        val rsvInfo = getRsvInfo(changeInfo.id)

        //最終更新日の確認
        if(rsvInfo.lastUpdate != changeInfo.lastUpdate){
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

        //SQL呼び出し
        reservationRepository.save(rsvInfo)

        return rsvInfo.reservationId
    }

    /**
     * 予約取消処理
     */
    fun delete(id: String, lastUpdate: Timestamp, request: HttpServletRequest): String {
        println("【予約取消】予約番号：$id")

        //予約情報取得
        val rsvInfo = getRsvInfo(id)

        //最終更新日の確認
        if(rsvInfo.lastUpdate != lastUpdate){
            println("最終更新日アンマッチ")
            throw Exception()
        }

        //取消処理
        rsvInfo.status = 4
        reservationRepository.save(rsvInfo)

        return rsvInfo.reservationId
    }

    /**
     * 予約変更、取消フロー用の予約情報取得処理
     *
     * 予約番号とUUIDを用いて予約検索を行う
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
}