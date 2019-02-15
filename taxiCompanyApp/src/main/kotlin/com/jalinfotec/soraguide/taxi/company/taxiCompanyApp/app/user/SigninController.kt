package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class SigninController {

    // ログイン画面表示
    @GetMapping(value = ["/", "/login"])
    fun index( mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        return mav
    }

    // 認証失敗時のログイン画面表示
    @GetMapping(value = ["/login-error"])
    fun loginError(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        // ログインエラーテキストを表示させる
        mav.addObject("isError", true)
        return mav
    }

    // ログアウト後のログイン画面表示
    @GetMapping(value = ["/logout"])
    fun logout(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        mav.addObject("isLogout", true)
        return mav
    }
}