package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class SigninController {

    // 認証前のログイン画面表示
    @RequestMapping(value = ["/", "/user"], method = [RequestMethod.GET])
    fun index( mav: ModelAndView): ModelAndView {
        mav.viewName = "user"
        return mav
    }

    // 認証失敗時のログイン画面表示
    @RequestMapping(value = ["/user-error"], method = [RequestMethod.GET])
    fun loginError(mav: ModelAndView): ModelAndView {
        mav.viewName = "user"
        // ログインエラーテキストを表示させる
        mav.addObject("isError", true)
        return mav
    }

    //
    @RequestMapping(value = ["/user-signup"], method = [RequestMethod.GET])
    fun signupSuccess(mav: ModelAndView): ModelAndView {
        mav.viewName = "user"
        mav.addObject("isSignup", true)
        return mav
    }
    //
    @RequestMapping(value = ["/user-logout"], method = [RequestMethod.GET])
    fun logout(mav: ModelAndView): ModelAndView {
        mav.viewName = "user"
        mav.addObject("isLogout", true)
        return mav
    }
}