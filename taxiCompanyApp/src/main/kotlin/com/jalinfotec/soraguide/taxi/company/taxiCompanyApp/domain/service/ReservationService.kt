package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class ReservationService {
    @Autowired
    private lateinit var repository: ReservationRepository

    @Transactional(readOnly = true)
    fun getListDefault(companyId: String): MutableList<ReservationInformation> {
        println("companyId: $companyId")
        return repository.findByCompanyIdOrderByDateAscTimeAsc(companyId)
    }
    @Transactional
    fun getDetail(companyId: String, reservationId: String): ReservationInformation? {
        return repository.findByCompanyIdAndId(companyId, reservationId)
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