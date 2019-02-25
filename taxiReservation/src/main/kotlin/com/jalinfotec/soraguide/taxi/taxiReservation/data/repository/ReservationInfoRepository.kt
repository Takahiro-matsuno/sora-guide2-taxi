package com.jalinfotec.soraguide.taxi.taxiReservation.data.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.*


@Repository
interface ReservationInfoRepository : JpaRepository<ReservationInformation, String> {
    /**
     *予約完了時用の検索処理
     *以下条件の予約情報を取得する
     *  1.電話番号が一致する
     *  2.メールアドレスが一致する
     *  3.ステータスがキャンセル待ち、完了ではないこと（値が4,5ではない、3以下であること）
     *  4.乗車日が今日以降であること
     */
    //fun findByPhoneAndMailAndStatusLessThanAndDateGreaterThanEqualOrderByIdAsc(
    //        phone: String, mail: String, status: Int, date: Date): MutableList<ReservationInformation>

    fun findByUuidAndStatusLessThanAndRideOnDateGreaterThanEqualOrderByReservationIdAsc(
            uuid: String, status: Int, date: Date): MutableList<ReservationInformation>

    fun findByReservationIdAndUuid(id: String, uuid: String): Optional<ReservationInformation>
}