package com.jalinfotec.soraguide.taxi.taxiReservation.data.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.*


@Repository
interface ReservationInfoRepository : JpaRepository<ReservationInformation, String> {
    /**
     *予約一覧取得用の検索処理
     *以下条件の予約情報を取得し、乗車日、乗車時間でソートする
     *  1.UUIDが一致する
     *  2.乗車日が今日以降であること
     */
    fun findByUuidAndRideOnDateGreaterThanEqualOrderByRideOnDateAscRideOnTimeAsc(
            uuid: String, date: Date): MutableList<ReservationInformation>

    /**
     * 予約詳細取得処理（予約アプリ用）
     */
    fun findByReservationIdAndUuid(id: String, uuid: String): Optional<ReservationInformation>

    /**
     * 夜間自動ステータス更新用クエリ
     *
     * ステータスが完了、またはキャンセル済みではない、
     * かつ乗車日が過去の予約はステータスを完了とする
     */
    @Modifying
        @Query("UPDATE ReservationInformation r SET r.status = 6, r.lastUpdate = CURRENT_TIMESTAMP "
            + "where status <= :STATUS AND ride_on_date < CURRENT_DATE")
    fun autoUpdateStatus(@Param("STATUS") status: Int)
}