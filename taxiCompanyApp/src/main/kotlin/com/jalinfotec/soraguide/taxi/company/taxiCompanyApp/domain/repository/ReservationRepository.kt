package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<ReservationInformation, String> {
    //fun findByCompanyId(companyId: String): List<ReservationInformation>
    //fun findByDetail(companyId: String, reservationId: String): ReservationInformation?
}