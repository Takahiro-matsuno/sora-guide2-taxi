package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.CookieManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.NumberingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Date
import java.sql.Time
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ReservationCompleteService(
        private val bookingRepository: BookingInfoRepository,
        private val numberingRepository: NumberingRepository) {

    @Transactional(readOnly = false)
    fun complete(input: ReservationForm, request: HttpServletRequest, response: HttpServletResponse): String {
        val bookingInfo = setBooking(input)
        println("【予約完了】予約ID：${bookingInfo.id}")
        bookingRepository.save(bookingInfo)
        cookieUpdate(bookingInfo, request, response)
        return bookingInfo.id
    }

    fun setBooking(input: ReservationForm): BookingInformation {
        return BookingInformation(
                id = setId(),

                //予約時のステータスは1で固定
                status = 1,

                //入力データをセット
                date = input.date,
                time = Time.valueOf(input.time + ":00"),
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
    fun setId(): String {
        val numbering = numberingRepository.findByName("booking_info")[0]
        val result = String.format("%010d", numbering.nextValue)

        numbering.nextValue++
        numberingRepository.save(numbering)

        return result
    }

    fun cookieUpdate(inputBookingInfo: BookingInformation, request: HttpServletRequest, response: HttpServletResponse) {
        val cookieManager = CookieManager()

        //以下条件の予約情報を取得する
        //1.予約時の電話番号と一致する
        //2.予約時のメールアドレスと一致する
        //3.ステータスがキャンセル待ち(4)、または完了(5)ではない
        //4.乗車日が過去日でない
        val bookingInfoList =
                bookingRepository.findByPhoneAndMailAndStatusLessThanAndDateGreaterThanEqualOrderByIdAsc(
                        inputBookingInfo.phone, inputBookingInfo.mail, 4, Date(Calendar.getInstance().timeInMillis)
                )

        //取得した予約情報から予約番号をカンマ区切りでString変数に格納する
        val idList = mutableListOf<String>()
        for (book in bookingInfoList) {
            println("【Cookie更新】予約ID：${book.id}")
            idList.add(book.id)
        }
        val idStr = idList.joinToString("-")

        //Cookieの値を更新する（名前は"rsvId"）
        cookieManager.setCookie(request, response, idStr)
    }
}