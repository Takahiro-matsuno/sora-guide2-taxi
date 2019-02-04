package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationService {
    @Autowired
    private lateinit var repository: ReservationRepository

    @Transactional
    fun getList(companyId: String): List<ReservationInformation> {
        return repository.findByCompanyId(companyId)
    }
    @Transactional
    fun getDetail(companyId: String, reservationId: String): ReservationInformation? {
        return repository.findByDetail(companyId, reservationId)
    }
    @Transactional
    fun update(reservation: ReservationInformation): Boolean {
        return true
    }
}