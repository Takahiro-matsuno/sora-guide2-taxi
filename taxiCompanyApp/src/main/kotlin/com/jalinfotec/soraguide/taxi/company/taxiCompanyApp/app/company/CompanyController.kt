package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.company

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class CompanyController {

    // 会社情報画面の表示
    @RequestMapping(value = ["/company/setting"], method = [RequestMethod.GET])
    fun getCompany(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/companySetting"
        return mav
    }

    // 会社情報の更新
    @RequestMapping(value = ["company/update"], method = [RequestMethod.POST])
    fun updateCompanyInfo(): String {
        return "forward:/compnay/setting" // success
        //return "forward:/error" // failed
    }
}