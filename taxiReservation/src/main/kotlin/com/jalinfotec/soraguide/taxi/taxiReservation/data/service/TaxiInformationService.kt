package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaxiInformationService(
        private val taxiRepository: TaxiInfoRepository
) {
    @Transactional
    fun getTaxiNameList(): MutableList<String> {
        val taxiList = taxiRepository.findAllByOrderById()
        val taxiNameList = mutableListOf<String>()

        for (taxi in taxiList) {
            taxiNameList.add(taxi.companyName)
        }

        return taxiNameList
    }

    @Transactional
    fun getTaxiInfoFromCompanyName(companyName: String): TaxiInformation {
        val taxiInfo = taxiRepository.findByCompanyName(companyName)

        return if (taxiInfo.isPresent) {
            taxiInfo.get()
        } else {
            throw Exception()
        }
    }

    @Transactional
    fun getCompanyName(companyId: String): String {
        val taxiInfo = taxiRepository.findById(companyId)

        return if (taxiInfo.isPresent) {
            taxiInfo.get().companyName
        } else {
            throw Exception()
        }
    }

    @Transactional
    fun getTaxiInfo(companyId: String): TaxiInformation? {
        val taxiInfo = taxiRepository.findById(companyId)

        return if (taxiInfo.isPresent) {
            taxiInfo.get()
        } else {
            null
        }
    }
}