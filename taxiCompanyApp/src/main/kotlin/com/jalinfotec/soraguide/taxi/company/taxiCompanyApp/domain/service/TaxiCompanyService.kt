package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.TaxiCompanyForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.AccountRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.TaxiCompanyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaxiCompanyService(
        private val accountRepository: AccountRepository,
        private val taxiRepository: TaxiCompanyRepository
) {

    @Transactional(readOnly = true)
    fun getTaxiCompanyForm(companyId: String): TaxiCompanyForm? {
        val optional = taxiRepository.findById(companyId)
        return if (optional.isPresent) {
            TaxiCompanyForm(optional.get().companyName)
        } else null
    }

    @Transactional
    fun updateTaxiCompanyInformation(taxiForm: TaxiCompanyForm, user: UserAccount): Boolean {

        val account = accountRepository.findByCompanyIdAndUsername(user.getCompanyId(), user.username) ?: return false
        val optional = taxiRepository.findById(account.companyId)

        if (!optional.isPresent) {
            // タクシー会社が見つからない場合は終了
            return false
        }
        val taxiCompany = optional.get()
        taxiCompany.companyName = taxiForm.companyName
        // 会社名を変更する
        taxiRepository.save(taxiCompany)
        return true
    }
}