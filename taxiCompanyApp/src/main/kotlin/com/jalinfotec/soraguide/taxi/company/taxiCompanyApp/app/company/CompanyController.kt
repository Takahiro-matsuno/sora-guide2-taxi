package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.company

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class CompanyController {

    // 会社情報画面の表示
    @GetMapping(value = ["/company/setting"])
    fun getCompany(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {
        mav.viewName = "contents/companySetting"
        return mav
    }

    // 会社情報の更新
    @PutMapping(value = ["company/update"])
    fun updateCompanyInfo(): String {
        return "forward:/compnay/setting" // success
        //return "forward:/error" // failed
    }
}