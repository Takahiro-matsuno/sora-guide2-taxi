package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<Account, Long> {
    fun findByUsername(userName: String): Account?
}