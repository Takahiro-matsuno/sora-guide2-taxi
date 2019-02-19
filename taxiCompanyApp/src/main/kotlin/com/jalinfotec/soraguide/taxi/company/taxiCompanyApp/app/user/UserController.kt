package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.UserSettingForm
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

    // パスワード変更画面表示
    @GetMapping(value = ["/user/setting"])
    fun getUser(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        val account = userService.findByUsername(user.username)

        return if (account == null) {
            // TODO エラー
            mav
        } else {
            mav.viewName = "contents/userSetting"
            mav.addObject("usForm", UserSettingForm())
            mav
        }
    }

    // パスワード変更
    @PostMapping(value = ["/user/password-change"])
    fun updateUser(
            @AuthenticationPrincipal user: UserAccount,
            @ModelAttribute(value = "usForm") usForm: UserSettingForm,
            mav: ModelAndView
    ): ModelAndView {
        return if (userService.changePassword(user.username, usForm)) {
            mav.viewName = "/contents/userSetting"
            mav.addObject("usForm", UserSettingForm())
            mav
        } else {
            // TODO エラー
            mav
        }
    }
}