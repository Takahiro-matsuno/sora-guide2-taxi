package com.jalinfotec.soraguide.taxi.taxiReservation.data.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Date


@Repository
interface BookingInfoRepository : JpaRepository<BookingInformation, String> {
    fun findByStatusAndDateGreaterThanEqualOrderByIdAsc(status: Int, date: Date):MutableList<BookingInformation>
}