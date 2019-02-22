package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.stereotype.Service
import java.sql.Time
import java.sql.Timestamp

@Service
class ReservationChangeService(
        private val reservationRepository: ReservationInfoRepository
) {

    fun change(changeInfo: ChangeForm): String {
        println("【予約変更】予約ID：${changeInfo.id}")

        val rsvInfoOptional = reservationRepository.findById(changeInfo.id)

        if (!rsvInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }

        val rsvInfo = rsvInfoOptional.get()
        //Time型の形式揃え
        if (changeInfo.time.length == 5) {
            changeInfo.time += ":00"
        }

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

        reservationRepository.save(rsvInfo)

        return rsvInfo.id
    }

    fun delete(id: String) : String {
        val bookingInfoOptional = reservationRepository.findById(id)

        val beforeInfo = bookingInfoOptional.get()
        beforeInfo.status = 4
        reservationRepository.save(beforeInfo)

        return beforeInfo.id
    }


}