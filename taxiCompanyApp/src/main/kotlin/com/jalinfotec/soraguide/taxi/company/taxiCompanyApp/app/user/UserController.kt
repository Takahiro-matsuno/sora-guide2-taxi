package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class UserController {

    @Autowired
    lateinit var userService: UserAccountService

    //
    @GetMapping(value = ["/user/setting"])
    fun getUser(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        val account = userService.findByUsername(user.username)
        if (account == null) {
            // ユーザーなし
            mav.viewName = "contents/error"
            mav.addObject("isUserNotFound", true)
            mav.addObject("username", user.username)
            return mav
        }

        mav.viewName = "contents/userSetting"
        mav.addObject("account", account)
        return mav
    }

    //
    @PostMapping(value = ["/user/update"])
    fun updateUser(
            @AuthenticationPrincipal user: UserAccount,
            @RequestBody body: String,
            mav: ModelAndView
    ): ModelAndView {

        // TODO ボディー読み込み
        // TODO アップデート処理

        return mav
    }
}