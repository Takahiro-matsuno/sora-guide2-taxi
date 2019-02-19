package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class IndexController {

    //
    @GetMapping(value = ["/index"])
    fun getCompany(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/index"
        return mav
    }
}