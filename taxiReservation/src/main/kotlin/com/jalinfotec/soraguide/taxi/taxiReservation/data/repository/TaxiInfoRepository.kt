package com.jalinfotec.soraguide.taxi.taxiReservation.data.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.TaxiInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface TaxiInfoRepository : JpaRepository<TaxiInformation, String> {
    fun findByCompanyName(companyName: String): TaxiInformation
}