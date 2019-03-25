package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ReservationRepository : JpaRepository<ReservationInformation, String> {
    // 参考：https://qiita.com/sndr/items/af7d12be264c2cc4b252
    fun findByCompanyIdOrderByRideOnDateAscRideOnTimeAsc(companyId: String): MutableList<ReservationInformation>

    fun findByCompanyIdAndReservationId(companyId: String, reservationId: String): ReservationInformation?
    fun findByCompanyIdAndStatusInOrderByRideOnDateAscRideOnTimeAsc(companyId: String, status: MutableList<Int>)
            : MutableList<ReservationInformation>

    /**
     * 夜間自動ステータス更新用クエリ
     *
     * ステータスが完了、またはキャンセル済みではない、
     * かつ乗車日が過去の予約はステータスを完了とする
     *
     * CURRENT_TIMESTAMPの後の半角スペースは重要
     */
    @Modifying
    @Query("UPDATE ReservationInformation r SET r.status = 6, r.lastUpdate = CURRENT_TIMESTAMP "
            + "where status <= :STATUS AND ride_on_date < CURRENT_DATE")
    fun autoUpdateStatus(@Param("STATUS") status: Int)
}