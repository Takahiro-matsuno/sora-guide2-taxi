package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Time
import java.util.*
import kotlin.NoSuchElementException

@Service
class ReservationDetailService(
        private val bookingRepository: BookingInfoRepository,
        private val taxiRepository: TaxiInfoRepository,
        private var bookingInfo: Optional<BookingInformation>
) {

    var taxiCompanyName: String = ""
    var statusText: String = ""

    fun getDetail(id: String): Optional<BookingInformation> {
        println("【予約情報取得】予約ID：$id")

        //DBから引数のIDとマッチする予約情報を取得
        bookingInfo = bookingRepository.findById(id)
        //タクシー会社IDからタクシー会社名を取得
        try {
            taxiCompanyName = taxiRepository.findById(bookingInfo.get().company_id).get().name
            statusTextSet(bookingInfo.get().status)
        }catch (e:NoSuchElementException){
            taxiCompanyName = ""
            statusText = ""
        }

        return bookingInfo
    }

    fun getChangeDetail(id: String): ReservationForm {
        val bookingInfo = getDetail(id).get()

        return ReservationForm(
                date = bookingInfo.date,
                time = bookingInfo.time.toString(),
                adult = bookingInfo.adult,
                child = bookingInfo.child,
                taxi_number = bookingInfo.taxi_number,
                company_id = bookingInfo.company_id,
                destination = bookingInfo.destination.trim(),
                name = bookingInfo.name.trim(),
                phonetic = bookingInfo.phonetic.trim(),
                phone = bookingInfo.phone.trim(),
                mail = bookingInfo.mail.trim(),
                mailCheck = bookingInfo.mail.trim(),
                comment = bookingInfo.comment.trim()
        )
    }

    fun detailCertificates(id:String,mail:String):Optional<BookingInformation>{
        val bookingInfo = getDetail(id)
        if(!bookingInfo.isPresent){
            return Optional.empty()
        }
        if(bookingInfo.get().mail.trim()!= mail){
            return Optional.empty()
        }
        return bookingInfo
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