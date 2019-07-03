package com.jalinfotec.soraguide.taxi.taxiReservation.domain.repository

import com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity.Numbering
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NumberingRepository : JpaRepository<Numbering, String>