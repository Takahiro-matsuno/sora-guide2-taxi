package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReservationDetailService {
    @Autowired
    var bookingRepository: BookingInfoRepository? = null

    @Autowired
    var taxiRepository: TaxiInfoRepository? = null

    var bookingInfo = BookingInformation()

    //var rideOnDate: String = ""
    //var rideOnTime: String = ""
    var taxiCompanyName: String = ""
    var statusText:String = ""

    fun getDetail(id: String): BookingInformation? {
        //DBから引数のIDとマッチする予約情報を取得
        bookingInfo = bookingRepository?.findById(id)?.get() ?: bookingInfo
        //タクシー会社IDからタクシー会社名を取得
        taxiCompanyName = taxiRepository?.findById(bookingInfo.company_id)?.get()!!.name
        statusTextSet(bookingInfo.status)
        return bookingRepository?.findById(id)?.get()
    }

    fun statusTextSet(code:Int){
        statusText = when(code){
            1 -> "受付中"
            2 -> "予約確定"
            3 -> "配車中"
            4 -> "キャンセル済み"
            5 -> "完了"
            else -> "その他"
        }
    }
}