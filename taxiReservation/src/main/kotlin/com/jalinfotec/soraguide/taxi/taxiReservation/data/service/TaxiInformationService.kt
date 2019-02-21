package com.jalinfotec.soraguide.taxi.taxiReservation.data.service

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Service

@Service
class TaxiInformationService(
        private val taxiRepository: TaxiInfoRepository
) {
    fun getTaxiNameList(): MutableList<String> {
        val taxiList = taxiRepository.findAll()
        val taxiNameList = mutableListOf<String>()

        for (taxi in taxiList) {
            taxiNameList.add(taxi.companyName)
        }

        return taxiNameList
    }

    fun getCompanyId(companyName: String): String {
        return taxiRepository.findByCompanyName(companyName).id
    }

}