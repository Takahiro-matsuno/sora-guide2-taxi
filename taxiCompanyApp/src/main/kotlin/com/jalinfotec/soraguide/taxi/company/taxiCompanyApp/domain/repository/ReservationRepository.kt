package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<ReservationInformation, String> {
    // 参考：https://qiita.com/sndr/items/af7d12be264c2cc4b252
    fun findByCompanyIdOrderByDateAscTimeAsc(companyId: String):MutableList<ReservationInformation>
    fun findByCompanyIdAndId(companyId: String, reservationId: String): ReservationInformation?
}