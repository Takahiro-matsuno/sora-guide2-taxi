package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.company

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.TaxiCompanyForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.TaxiCompanyService
import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class CompanyController(
        val taxiService: TaxiCompanyService,
        private val telemetry: TelemetryClient
) {
    // 会社情報画面の表示
    @GetMapping(value = ["/company/setting"])
    fun getCompany(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        val taxiForm = taxiService.getTaxiCompanyForm(user.getCompanyId())
        return if (taxiForm == null) {
            // タクシー会社情報取得失敗
            telemetry.trackEvent("CompanyInfo_Fetch Failure")
            mav.viewName = "/contents/error"
            mav.addObject("errorMessage", "会社情報の取得に失敗しました")
            mav
        } else {
            telemetry.trackEvent("CompanyInfo_Fetch Success")
            mav.viewName = "contents/companySetting"
            mav.addObject("taxiForm", taxiForm)
            mav
        }
    }

    // 会社情報の更新
    // 会社情報画面の表示
    @PostMapping(value = ["company/setting"])
    fun updateCompanyInfo(
            @AuthenticationPrincipal user: UserAccount,
            @ModelAttribute taxiForm: TaxiCompanyForm,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/companySetting"
        if (taxiService.updateTaxiCompanyInformation(taxiForm, user)) {
            telemetry.trackEvent("Updated_Company_info")
            mav.addObject("message", "会社情報を更新しました。")
            mav.addObject("taxiForm", taxiForm)
        } else {
            telemetry.trackEvent("Failure_update_Company_info")
            mav.addObject("message", "更新に失敗しました。")
        }
        return mav
    }

}