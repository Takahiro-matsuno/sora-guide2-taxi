package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.ReservationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationService(
        private val repository: ReservationRepository
) {

    @Transactional(readOnly = true)
    fun getListDefault(companyId: String): MutableList<ReservationInformation> {
        println("companyId: $companyId")
        return repository.findByCompanyIdOrderByRideOnDateAscRideOnTimeAsc(companyId)
    }
    @Transactional
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

/*
    */
    /*

    @Transactional
    fun update(reservation: ReservationInformation): Boolean {
        return true
    }
    */
}