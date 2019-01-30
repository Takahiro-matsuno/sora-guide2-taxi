package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.NumberingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Time

@Service
class ReservationCompleteService(
        private val bookingRepository :BookingInfoRepository,
        private val numberingRepository: NumberingRepository) {
    /*
    @Autowired
    var bookingRepository: BookingInfoRepository? = null
    @Autowired
    var numberingRepository = NumberingRepository()
    */

    var bookingInfo = BookingInformation()

    fun setBooking(input: ReservationForm) {
        bookingInfo = BookingInformation(
                id = setId(),

                //予約時のステータスは1で固定
                status = 1,

                //入力データをセット
                date = input.date,
                //TODO タイム型がうまくいかないから何とかしたい
                time = null,//Time.valueOf(input.time),
                adult = input.adult,
                child = input.child,
                taxi_number = input.taxi_number,
                company_id = input.company_id,
                destination = input.destination,
                name = input.name,
                phonetic = input.phonetic,
                phone = input.phone,
                mail = input.mail,
                comment = input.comment,

                //タクシー会社入力欄はブランク
                car_number = "",
                car_contact = "",
                notice = ""
        )
    }

    @Transactional(readOnly = false)
    fun complete() {
        bookingRepository.save(bookingInfo)
    }

    @Transactional(readOnly = false)
    fun setId():String{
        val numbering = numberingRepository.findByName("booking_info")[0]
        val result = String.format("%010d",numbering.nextValue)

        numbering.nextValue++
        numberingRepository.save(numbering)


        return result
    }


}