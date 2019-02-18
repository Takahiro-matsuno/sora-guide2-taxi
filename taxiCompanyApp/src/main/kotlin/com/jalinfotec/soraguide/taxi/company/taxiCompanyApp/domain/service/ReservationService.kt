package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.ReservationRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationService(
        private val repository: ReservationRepository
) {

    // 予約一覧を取得する
    @Transactional(readOnly = true)
    fun getListDefault(companyId: String): ArrayList<ReservationForm> {

        val results = repository.findByCompanyIdOrderByRideOnDateAscRideOnTimeAsc(companyId)
        val formList = ArrayList<ReservationForm>()

        for (rsvInfo in results) {
            val rsvForm = convertRsvInfo2RsvForm(rsvInfo) ?: continue
            formList.add(rsvForm)
        }
        return formList
    }
    @Transactional(readOnly = true)
    fun getDetail(companyId: String, reservationId: String): ReservationInformation? {
        return repository.findByCompanyIdAndReservationId(companyId, reservationId)
    }
    @Transactional
    fun updateDetail(rsvInfo: ReservationInformation): Boolean {
        val optional = repository.findById(rsvInfo.reservationId)
        return if (optional.isPresent) {
            repository.save(rsvInfo)
            true
        } else false
    }

    private fun convertRsvInfo2RsvForm(rsvInfo: ReservationInformation): ReservationForm? {

        // 予約ステータスの置き換え
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        return ReservationForm(
                rsvInfo.reservationId.trim(),
                statusName,
                rsvInfo.rideOnDate,
                rsvInfo.rideOnTime,
                rsvInfo.adult,
                rsvInfo.child,
                rsvInfo.carDispatchNumber,
                rsvInfo.destination.trim(),
                rsvInfo.passengerName.trim(),
                rsvInfo.passengerPhonetic.trim(),
                rsvInfo.passengerContact.trim(),
                rsvInfo.passengerMail.trim(),
                rsvInfo.comment.trim(),
                rsvInfo.carNumber.trim(),
                rsvInfo.carContact.trim(),
                rsvInfo.notice.trim()
        )

    }
    private fun convertRsvForm2RsvInfo(rsvInfo: ReservationInformation, rsvForm: ReservationForm): ReservationInformation? {
        return null
    }


/*
    */
    /*

    @Transactional
    fun update(reservation: ReservationInformation): Boolean {
        return true
    }
    */
}