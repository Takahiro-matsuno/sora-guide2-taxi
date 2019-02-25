package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.LastUpdateManager
import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.UuidManager
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
        val rsvInfo = getRsvInfo(id, request)

        return ChangeForm(
                id = rsvInfo.id,
                date = rsvInfo.date,
                time = rsvInfo.time.toString(),
                adult = rsvInfo.adult,
                child = rsvInfo.child,
                car_dispatch = rsvInfo.car_dispatch_number,
                destination = rsvInfo.destination.trim(),
                phone = rsvInfo.phone.trim(),
                mail = rsvInfo.mail.trim(),
                mailCheck = rsvInfo.mail.trim(),
                comment = rsvInfo.comment.trim()
        )
    }

    /**
     * 予約変更処理
     */
    fun change(changeInfo: ChangeForm, request: HttpServletRequest): String {
        println("【予約変更】予約番号：${changeInfo.id}")

        //予約情報取得
        val rsvInfo = getRsvInfo(changeInfo.id, request)

        //最終更新日の確認
        val lastUpdateManager = LastUpdateManager()
        if(!lastUpdateManager.checkSession(rsvInfo.last_update, request)){
            println("最終更新日アンマッチ")
            return rsvInfo.id
        }

        //Time型の形式揃え
        if (changeInfo.time.length == 5) {
            changeInfo.time += ":00"
        }

        //FormからEntityに変更項目のみ詰め替え
        rsvInfo.date = changeInfo.date
        rsvInfo.time = Time.valueOf(changeInfo.time)
        rsvInfo.adult = changeInfo.adult
        rsvInfo.child = changeInfo.child
        rsvInfo.car_dispatch_number = changeInfo.car_dispatch
        rsvInfo.destination = changeInfo.destination
        rsvInfo.phone = changeInfo.phone
        rsvInfo.mail = changeInfo.mail
        rsvInfo.comment = changeInfo.comment
        rsvInfo.last_update = Timestamp(System.currentTimeMillis())

        //SQL呼び出し
        reservationRepository.save(rsvInfo)

        return rsvInfo.id
    }

    /**
     * 予約取消処理
     */
    fun delete(id: String, request: HttpServletRequest): String {
        println("【予約取消】予約番号：$id")

        //予約情報取得
        val rsvInfo = getRsvInfo(id, request)

        //取消処理
        rsvInfo.status = 4
        reservationRepository.save(rsvInfo)

        return rsvInfo.id
    }

    /**
     * 予約変更、取消フロー用の予約情報取得処理
     *
     * 予約番号とUUIDを用いて予約検索を行う
     * 予約情報が存在しない場合はエラーを投げる。
     */
    fun getRsvInfo(id: String, request: HttpServletRequest): ReservationInformation {
        val uuid = UuidManager().getUuid(request) ?: ""

        val rsvInfoOptional = reservationRepository.findByIdAndUuid(id, uuid)

        //予約が存在しない場合、エラー
        if (!rsvInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }

        return rsvInfoOptional.get()
    }
}