package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Time

@Service
class ReservationDetailService(
        private val bookingRepository: BookingInfoRepository,
        private val taxiRepository: TaxiInfoRepository
) {

    var bookingInfo = BookingInformation()

    //var rideOnDate: String = ""
    //var rideOnTime: String = ""
    var taxiCompanyName: String = ""
    var statusText: String = ""

    fun getDetail(id: String): BookingInformation {
        //DBから引数のIDとマッチする予約情報を取得
        bookingInfo = bookingRepository.findById(id).get()
        //タクシー会社IDからタクシー会社名を取得
        taxiCompanyName = taxiRepository.findById(bookingInfo.company_id).get().name
        statusTextSet(bookingInfo.status)
        return bookingRepository.findById(id).get()
    }

    fun getChangeDetail(id: String): ReservationForm {
        val bookingInfo = getDetail(id)
        return ReservationForm(
                date = bookingInfo.date,
                time = bookingInfo.time.toString(),
                adult = bookingInfo.adult,
                child = bookingInfo.child,
                taxi_number = bookingInfo.taxi_number,
                company_id = bookingInfo.company_id,
                destination = bookingInfo.destination,
                name = bookingInfo.name,
                phonetic = bookingInfo.phonetic,
                phone = bookingInfo.phone,
                mail = bookingInfo.mail,
                mailCheck = bookingInfo.mail,
                comment = bookingInfo.comment
                )
    }

    fun statusTextSet(code: Int) {
        statusText = when (code) {
            1 -> "受付中"
            2 -> "予約確定"
            3 -> "配車中"
            4 -> "キャンセル済み"
            5 -> "完了"
            else -> "その他"
        }
    }
}