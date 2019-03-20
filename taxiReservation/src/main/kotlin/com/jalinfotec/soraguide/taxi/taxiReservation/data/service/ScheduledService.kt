package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScheduledService(
        private val reservationInfoRepository: ReservationInfoRepository
) {

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    fun autoStatusChange(){
        println("定期実行開始")

        reservationInfoRepository.updateTest(4)
    }
}