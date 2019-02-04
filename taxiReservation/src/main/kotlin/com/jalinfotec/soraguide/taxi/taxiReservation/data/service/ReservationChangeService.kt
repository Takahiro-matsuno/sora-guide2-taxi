package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import org.springframework.stereotype.Service
import java.sql.Time
import java.util.*

@Service
class ReservationChangeService(
        private val bookingRepository: BookingInfoRepository
) {
    var bookingInfo = BookingInformation()

    fun change(id: String, changeInfo: ReservationForm, name: String) {
        println("【予約変更】予約ID：$id")

        val bookingInfoOptional = bookingRepository.findById(id)
        changeInfo.name = name

        if (!changeValidate(changeInfo, bookingInfoOptional)) {
            throw Exception()
        }

        val beforeInfo = bookingInfoOptional.get()
        //Time型の形式揃え
        if (changeInfo.time.length == 5) {
            changeInfo.time += ":00"
        }

        bookingInfo = BookingInformation(
                id = beforeInfo.id,
                status = beforeInfo.status,
                company_id = beforeInfo.company_id,
                name = beforeInfo.name,
                phonetic = beforeInfo.phonetic,
                car_contact = beforeInfo.car_contact,
                car_number = beforeInfo.car_number,
                notice = beforeInfo.notice,

                date = changeInfo.date,
                time = Time.valueOf(changeInfo.time),
                adult = changeInfo.adult,
                child = changeInfo.child,
                taxi_number = changeInfo.taxi_number,
                destination = changeInfo.destination,
                phone = changeInfo.phone,
                mail = changeInfo.mail,
                comment = changeInfo.comment
        )

        bookingRepository.save(bookingInfo)
    }

    //予約変更前の妥当性チェック
    fun changeValidate(changeInfo: ReservationForm, bookingInfoOptional: Optional<BookingInformation>): Boolean {
        //検索にかからない場合
        if (!bookingInfoOptional.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            return false
        }
        //ID改ざん対策
        if (bookingInfoOptional.get().name.trim() != changeInfo.name.trim()) {
            println("ERROR：変更前後の予約で名前が一致しない")
            println("DB->${bookingInfoOptional.get().name.trim()}")
            println("入力->${changeInfo.name.trim()}")
            return false
        }
        return true
    }

    fun delete(id: String) {
        val beforeInfo = bookingRepository.findById(id).get()
        beforeInfo.status = 4
        bookingRepository.save(bookingInfo)
    }


}