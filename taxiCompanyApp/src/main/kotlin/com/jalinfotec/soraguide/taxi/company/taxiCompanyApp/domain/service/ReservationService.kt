package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class ReservationService {
    //@Autowired
    //private lateinit var repository: ReservationRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional(readOnly = true)
    fun getListDefault(companyId: String): List<ReservationInformation> {
        println("companyId: $companyId")

        return entityManager
                .createQuery(
                        "SELECT BI FROM booking_info BI " +
                                "WHERE BI.company_id = :companyId " +
                                "ORDER BY BI.date ASC, BI.time ASC",
                        ReservationInformation::class.java)
                .setParameter("companyId", companyId)
                .resultList
    }
/*
    @Transactional
    fun getListDefault(companyId: String): List<ReservationInformation> {
        return repository.findByCompanyId(companyId)
    }
    */
    /*
    @Transactional
    fun getDetail(companyId: String, reservationId: String): ReservationInformation? {
        return repository.findByDetail(companyId, reservationId)
    }
    @Transactional
    fun update(reservation: ReservationInformation): Boolean {
        return true
    }
    */
}