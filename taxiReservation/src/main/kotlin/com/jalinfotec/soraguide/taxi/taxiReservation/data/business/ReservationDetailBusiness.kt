package com.jalinfotec.soraguide.taxi.taxiReservation.data.business

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import org.springframework.beans.factory.annotation.Autowired

class ReservationDetailBusiness(id: String) {
    @Autowired
    var bookingInfoRepository: BookingInfoRepository? = null

    var rideOnDate:String = ""
    var rideOnTime:String = ""
    var taxiCompanyName: String = ""

    //TODO 受け取ったIDから予約情報をリポジトリの処理で予約情報をDBから取得する
    //TODO 予約情報の一部を画面表示用に成形して返す処理（日付？時間？タクシー会社名）
}