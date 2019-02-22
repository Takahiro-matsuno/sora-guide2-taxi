package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.UuidManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.DetailForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class ReservationDetailService(
        private val reservationRepository: ReservationInfoRepository,
        private val taxiRepository: TaxiInfoRepository
) {

    fun getDetail(id: String, request: HttpServletRequest): DetailForm? {
        println("【予約情報取得】予約ID：$id")

        //TODO UUIDが見つからない場合（Web遷移の場合？）の処理は一旦適当
        val uuid = UuidManager().getUuid(request) ?: ""

        //DBから引数のIDとマッチする予約情報を取得
        val reservationInfo = reservationRepository.findByIdAndUuid(id, uuid)
        //タクシー会社IDからタクシー会社名を取得
        val taxiCompanyName = taxiRepository.findById(reservationInfo.get().company_id)

        return if (reservationInfo.isPresent && taxiCompanyName.isPresent) {
            convertRsvInfo2RsvForm(reservationInfo.get(), taxiCompanyName.get().companyName)
        } else null
    }

    fun getChangeDetail(id: String): ChangeForm {
        //DBから引数のIDとマッチする予約情報を取得
        val rsvInfo = reservationRepository.findById(id).get()

        return ChangeForm(
                id = rsvInfo.id,
                date = rsvInfo.date,
                time = rsvInfo.time.toString(),
                adult = rsvInfo.adult,
                child = rsvInfo.child,
                car_dispatch = rsvInfo.car_dispatch_number,
                destination = rsvInfo.destination.trim(),
                phone = rsvInfo.phone.trim(),
                mail = rsvInfo.mail.trim(),
                mailCheck = rsvInfo.mail.trim(),
                comment = rsvInfo.comment.trim()
        )
    }

    fun detailCertificates(id: String, mail: String, request: HttpServletRequest): DetailForm? {
        val rsvInfo = getDetail(id, request) ?: return null
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