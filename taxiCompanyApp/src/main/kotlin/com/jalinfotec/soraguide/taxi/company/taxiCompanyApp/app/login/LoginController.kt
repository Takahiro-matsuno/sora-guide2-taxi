package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.login

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class LoginController {

    // TODO サーバー起動時１回目のログインが無効になる

    // 認証前のログイン画面表示
    @RequestMapping(value = ["/", "/login"], method = [RequestMethod.GET])
    fun index( mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        return mav
    }
    // 認証失敗時のログイン画面表示
    @RequestMapping(value = ["/login-error"], method = [RequestMethod.GET])
    fun loginError(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        // ログインエラーテキストを表示させる
        mav.addObject("isError", true)
        return mav
    }
    //
    @RequestMapping(value = ["/login-signup"], method = [RequestMethod.GET])
    fun signupSuccess(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        mav.addObject("isSignup", true)
        return mav
    }
    //
    @RequestMapping(value = ["/login-logout"], method = [RequestMethod.GET])
    fun logout(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        mav.addObject("isLogout", true)
        return mav
    }
}