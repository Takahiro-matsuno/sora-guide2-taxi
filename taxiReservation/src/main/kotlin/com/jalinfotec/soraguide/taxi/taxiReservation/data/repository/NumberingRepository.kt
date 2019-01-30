package com.jalinfotec.soraguide.taxi.taxiReservation.data.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.Numbering
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NumberingRepository : JpaRepository<Numbering, String>