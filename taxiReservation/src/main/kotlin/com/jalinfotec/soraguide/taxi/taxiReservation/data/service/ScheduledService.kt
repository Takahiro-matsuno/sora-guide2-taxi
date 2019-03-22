package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScheduledService(
        private val reservationInfoRepository: ReservationInfoRepository
) {

    /**
     * ステータス自動変更処理呼び出し
     *
     * 夜間にステータスの自動変更処理を呼び出すメソッド
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tokyo")
    @Transactional
    fun autoUpdateStatus() {
        println("定期実行開始")

        reservationInfoRepository.autoUpdateStatus(4)
    }
}