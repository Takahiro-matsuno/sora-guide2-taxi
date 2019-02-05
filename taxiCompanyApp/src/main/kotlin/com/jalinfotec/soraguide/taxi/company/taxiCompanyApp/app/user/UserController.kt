package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class UserController {

    @Autowired
    lateinit var userService: UserAccountService
    //
    @RequestMapping(value = ["/user/setting"], method = [RequestMethod.GET])
    fun getUser(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/userSetting"
        return mav
    }
}