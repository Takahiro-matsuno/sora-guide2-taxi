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
    var bookingInfo = ReservationInformation()

    fun change(changeInfo: ReservationForm): String {
        println("【予約変更】予約ID：${changeInfo.id}")

        val bookingInfoOptional = reservationRepository.findById(changeInfo.id)

        if (!changeValidate(changeInfo, bookingInfoOptional)) {
            throw Exception()
        }

        val beforeInfo = bookingInfoOptional.get()
        //Time型の形式揃え
        if (changeInfo.time.length == 5) {
            changeInfo.time += ":00"
        }

        bookingInfo = ReservationInformation(
                id = beforeInfo.id,
                status = beforeInfo.status,
                company_id = beforeInfo.company_id,
                passenger_name = beforeInfo.passenger_name,
                passenger_phonetic = beforeInfo.passenger_phonetic,
                car_contact = beforeInfo.car_contact,
                car_number = beforeInfo.car_number,
                notice = beforeInfo.notice,

                date = changeInfo.date,
                time = Time.valueOf(changeInfo.time),
                adult = changeInfo.adult,
                child = changeInfo.child,
                car_dispatch_number = changeInfo.car_dispatch,
                destination = changeInfo.destination,
                phone = changeInfo.phone,
                mail = changeInfo.mail,
                comment = changeInfo.comment
        )

        reservationRepository.save(bookingInfo)

        return bookingInfo.id
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