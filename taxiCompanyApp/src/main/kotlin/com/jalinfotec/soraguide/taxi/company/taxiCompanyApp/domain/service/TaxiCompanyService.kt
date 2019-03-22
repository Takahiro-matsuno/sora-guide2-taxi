package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.TaxiCompanyForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.AccountRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.TaxiCompanyRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaxiCompanyService(
        private val accountRepository: AccountRepository,
        private val taxiRepository: TaxiCompanyRepository
) {

    @Transactional(readOnly = true)
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getTaxiCompanyForm(companyId: String): TaxiCompanyForm? {
        val optional = taxiRepository.findById(companyId)
        return if (optional.isPresent) {
            TaxiCompanyForm(optional.get().companyName, optional.get().contact)
        } else null
    }

    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun updateTaxiCompanyInformation(taxiForm: TaxiCompanyForm, user: UserAccount): Boolean {

        val account = accountRepository.findByCompanyIdAndUsername(user.getCompanyId(), user.username) ?: return false
        val optional = taxiRepository.findById(account.companyId)

        if (!optional.isPresent) {
            // タクシー会社が見つからない場合は終了
            return false
        }

        val taxiCompany = optional.get()
        taxiCompany.companyName = taxiForm.companyName
        taxiCompany.contact = taxiForm.companyContact

        // 会社情報を変更する
        taxiRepository.save(taxiCompany)
        return true
    }

    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getCompanyInfo(companyId: String): TaxiInformation? {
        val companyInfoOptional = taxiRepository.findById(companyId)

        return if (companyInfoOptional.isPresent) {
            companyInfoOptional.get()
        } else {
            null
        }
    }
}