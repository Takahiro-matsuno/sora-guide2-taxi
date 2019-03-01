package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.data.domain.Sort
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
    fun getCompanyId(companyName: String): String {
        return taxiRepository.findByCompanyName(companyName).id
    }
}