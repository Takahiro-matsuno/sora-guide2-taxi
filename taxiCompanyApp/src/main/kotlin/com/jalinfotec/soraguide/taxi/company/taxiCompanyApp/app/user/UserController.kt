package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
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


        // TODO ユーザー情報取得
        val account = userService.findByUsername(user.username)
        if (account == null) {
            // TODO エラー処理
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
    @RequestMapping(value = ["/user/update"], method = [RequestMethod.POST])
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