package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.stereotype.Service
import java.sql.Time
import java.util.*

@Service
class ReservationChangeService(
        private val reservationRepository: ReservationInfoRepository
) {

    fun change(changeInfo: ReservationForm): String {
        println("【予約変更】予約ID：${changeInfo.id}")

        val bookingInfoOptional = reservationRepository.findById(changeInfo.id)

        if (!changeValidate(changeInfo, bookingInfoOptional)) {
            throw Exception()
        }

        val rsvInfo = bookingInfoOptional.get()
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

        /*
        bookingInfo = ReservationInformation(
                id = rsvInfo.id,
                status = rsvInfo.status,
                company_id = rsvInfo.company_id,
                passenger_name = rsvInfo.passenger_name,
                passenger_phonetic = rsvInfo.passenger_phonetic,
                car_contact = rsvInfo.car_contact,
                car_number = rsvInfo.car_number,
                notice = rsvInfo.notice,

                date = changeInfo.date,
                time = Time.valueOf(changeInfo.time),
                adult = changeInfo.adult,
                child = changeInfo.child,
                car_dispatch_number = changeInfo.car_dispatch,
                destination = changeInfo.destination,
                phone = changeInfo.phone,
                mail = changeInfo.mail,
                comment = changeInfo.comment
        )*/

        reservationRepository.save(rsvInfo)

        return rsvInfo.id
    }

    //予約変更前の妥当性チェック
    fun changeValidate(changeInfo: ReservationForm, reservationInfoOptional: Optional<ReservationInformation>): Boolean {
        //検索にかからない場合
        if (!reservationInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            return false
        }
        //ID改ざん対策
        if (reservationInfoOptional.get().passenger_name.trim() != changeInfo.name.trim()) {
            println("ERROR：変更前後の予約で名前が一致しない")
            println("DB->${reservationInfoOptional.get().passenger_name.trim()}")
            println("入力->${changeInfo.name.trim()}")
            return false
        }
        return true
    }

    fun delete(id: String) : String {
        val bookingInfoOptional = reservationRepository.findById(id)

        val beforeInfo = bookingInfoOptional.get()
        beforeInfo.status = 4
        reservationRepository.save(beforeInfo)

        return beforeInfo.id
    }


}