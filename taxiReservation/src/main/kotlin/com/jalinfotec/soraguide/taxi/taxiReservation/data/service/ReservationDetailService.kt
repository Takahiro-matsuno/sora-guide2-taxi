package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class ReservationDetailService(
        private val reservationRepository: ReservationInfoRepository,
        private val taxiRepository: TaxiInfoRepository,
        private var reservationInfo: Optional<ReservationInformation>
) {

    // TODO 以下を含めたフォームを作る、後に消える
    var taxiCompanyName: String = ""
    var statusText: String = ""

    fun getDetail(id: String): Optional<ReservationInformation> {
        println("【予約情報取得】予約ID：$id")

        //DBから引数のIDとマッチする予約情報を取得
        reservationInfo = reservationRepository.findById(id)
        //タクシー会社IDからタクシー会社名を取得
        try {
            taxiCompanyName = taxiRepository.findById(reservationInfo.get().company_id).get().companyName
            statusTextSet(reservationInfo.get().status)
        } catch (e: NoSuchElementException) {
            taxiCompanyName = ""
            statusText = ""
        }

        return reservationInfo
    }

    fun getChangeDetail(id: String): ReservationForm {
        val bookingInfo = getDetail(id).get()
        val rsvForm = ReservationForm()
        rsvForm.id = bookingInfo.id
        rsvForm.date = bookingInfo.date

        return ReservationForm(
                id = bookingInfo.id,
                date = bookingInfo.date,
                time = bookingInfo.time.toString(),
                adult = bookingInfo.adult,
                child = bookingInfo.child,
                car_dispatch = bookingInfo.car_dispatch_number,
                company_name = bookingInfo.company_id,
                destination = bookingInfo.destination.trim(),
                name = bookingInfo.passenger_name.trim(),
                phonetic = bookingInfo.passenger_phonetic.trim(),
                phone = bookingInfo.phone.trim(),
                mail = bookingInfo.mail.trim(),
                mailCheck = bookingInfo.mail.trim(),
                comment = bookingInfo.comment.trim()
        )
    }

    fun detailCertificates(id: String, mail: String): Optional<ReservationInformation> {
        val bookingInfo = getDetail(id)
        if (!bookingInfo.isPresent) {
            return Optional.empty()
        }
        if (bookingInfo.get().mail.trim() != mail) {
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