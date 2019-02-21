package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.DetailForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.stereotype.Service

@Service
class ReservationDetailService(
        private val reservationRepository: ReservationInfoRepository,
        private val taxiRepository: TaxiInfoRepository
) {

    // TODO 以下を含めたフォームを作る、後に消える
    var taxiCompanyName: String = ""
    var statusText: String = ""

    fun getDetail(id: String): DetailForm? {
        println("【予約情報取得】予約ID：$id")

        //DBから引数のIDとマッチする予約情報を取得
        val reservationInfo = reservationRepository.findById(id)
        //タクシー会社IDからタクシー会社名を取得
        val taxiCompanyName = taxiRepository.findById(reservationInfo.get().company_id)

        return if (reservationInfo.isPresent && taxiCompanyName.isPresent) {
            convertRsvInfo2RsvForm(reservationInfo.get(), taxiCompanyName.get().companyName)
        } else null
    }

    fun getChangeDetail(id: String): ReservationForm {
        //DBから引数のIDとマッチする予約情報を取得
        val rsvInfo = reservationRepository.findById(id).get()
        val rsvForm = ReservationForm()
        rsvForm.id = rsvInfo.id
        rsvForm.date = rsvInfo.date

        return ReservationForm(
                id = rsvInfo.id,
                date = rsvInfo.date,
                time = rsvInfo.time.toString(),
                adult = rsvInfo.adult,
                child = rsvInfo.child,
                car_dispatch = rsvInfo.car_dispatch_number,
                company_name = rsvInfo.company_id,
                destination = rsvInfo.destination.trim(),
                name = rsvInfo.passenger_name.trim(),
                phonetic = rsvInfo.passenger_phonetic.trim(),
                phone = rsvInfo.phone.trim(),
                mail = rsvInfo.mail.trim(),
                mailCheck = rsvInfo.mail.trim(),
                comment = rsvInfo.comment.trim()
        )
    }

    fun detailCertificates(id: String, mail: String): DetailForm? {
        val rsvInfo = getDetail(id) ?: return null
        if (rsvInfo.mail.trim() != mail) {
            return null
        }
        return rsvInfo
    }

    fun convertRsvInfo2RsvForm(rsvInfo: ReservationInformation,
                               companyName: String): DetailForm? {

        // 予約ステータスの置き換え
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        return DetailForm(
                rsvInfo.id,
                statusName,
                rsvInfo.date.toString(),
                rsvInfo.time.toString(),
                rsvInfo.adult.toString(),
                rsvInfo.child.toString(),
                rsvInfo.car_dispatch_number.toString(),
                rsvInfo.destination,
                rsvInfo.passenger_name,
                rsvInfo.passenger_phonetic,
                rsvInfo.phone,
                rsvInfo.mail,
                rsvInfo.comment,
                rsvInfo.car_number,
                rsvInfo.car_contact,
                rsvInfo.notice
        )
    }
}