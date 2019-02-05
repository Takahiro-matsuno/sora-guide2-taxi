package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class IndexController {

    //
    @RequestMapping(value = ["/home"], method = [RequestMethod.GET])
    fun getCompany(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/index"
        return mav
    }
}