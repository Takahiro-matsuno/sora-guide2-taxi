package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Service
import java.sql.Time

@Service
class ReservationChangeService(
        private val bookingRepository: BookingInfoRepository,
        private val taxiRepository: TaxiInfoRepository
) {
    var bookingInfo = BookingInformation()

    fun change(id: String, changeInfo: ReservationForm) {
        val beforeInfo = bookingRepository.findById(id).get()

        //Time型の形式揃え
        if (changeInfo.time.length == 5) {
            changeInfo.time += ":00"
        }

        bookingInfo = BookingInformation(
                id = beforeInfo.id,
                status = beforeInfo.status,
                date = changeInfo.date,
                time = Time.valueOf(changeInfo.time),
                adult = changeInfo.adult,
                child = changeInfo.child,
                taxi_number = changeInfo.taxi_number,
                company_id = beforeInfo.company_id,
                destination = changeInfo.destination,
                name = beforeInfo.name,
                phonetic = beforeInfo.phonetic,
                phone = changeInfo.phone,
                mail = changeInfo.mail,
                comment = changeInfo.comment,
                car_contact = beforeInfo.car_contact,
                car_number = beforeInfo.car_number,
                notice = beforeInfo.notice
        )

        bookingRepository.save(bookingInfo)
    }

    fun delete(id: String) {
        val beforeInfo = bookingRepository.findById(id).get()
        beforeInfo.status = 4
        bookingRepository.save(bookingInfo)
    }


}