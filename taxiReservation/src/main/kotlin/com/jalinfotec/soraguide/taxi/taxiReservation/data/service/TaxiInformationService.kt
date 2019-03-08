package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.hibernate.exception.JDBCConnectionException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaxiInformationService(
        private val taxiRepository: TaxiInfoRepository
) {
    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getTaxiNameList(): MutableList<String> {
        val taxiList = taxiRepository.findAllByOrderById()
        val taxiNameList = mutableListOf<String>()

        for (taxi in taxiList) {
            taxiNameList.add(taxi.companyName)
        }

        return taxiNameList
    }

    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getTaxiInfoFromCompanyName(companyName: String): TaxiInformation {
        println("てすと！！！りとらいしたかどうかみたいのです！！！！")

        val taxiInfo = taxiRepository.findByCompanyName(companyName)

        return if (taxiInfo.isPresent) {
            taxiInfo.get()
        } else {
            throw Exception()
        }
    }

    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getCompanyName(companyId: String): String {
        val taxiInfo = taxiRepository.findById(companyId)

        return if (taxiInfo.isPresent) {
            taxiInfo.get().companyName
        } else {
            throw Exception()
        }
    }

    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getTaxiInfo(companyId: String): TaxiInformation? {
        val taxiInfo = taxiRepository.findById(companyId)

        return if (taxiInfo.isPresent) {
            taxiInfo.get()
        } else {
            null
        }
    }
}