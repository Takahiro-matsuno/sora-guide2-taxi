package com.jalinfotec.soraguide.taxi.taxiReservation.data.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import org.springframework.data.jpa.repository.JpaRepository
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

    fun findByReservationIdAndUuid(id: String, uuid: String): Optional<ReservationInformation>
}