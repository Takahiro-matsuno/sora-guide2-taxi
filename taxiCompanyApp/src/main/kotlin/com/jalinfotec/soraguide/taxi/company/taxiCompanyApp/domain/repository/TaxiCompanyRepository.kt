package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.TaxiInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaxiCompanyRepository: JpaRepository<TaxiInformation, String>