package com.jalinfotec.soraguide.taxi.taxiReservation.domain.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity.TaxiInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface TaxiInfoRepository : JpaRepository<TaxiInformation, String> {
    /**
     * タクシー情報取得
     *
     * 会社名から会社情報を取得
     */
    fun findByCompanyName(companyName: String): Optional<TaxiInformation>

    /**
     * 会社名をIDソートして全件取得
     */
    fun findAllByOrderById(): MutableList<TaxiInformation>
}