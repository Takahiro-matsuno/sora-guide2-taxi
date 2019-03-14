package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
class SigninController(
        private val messageSource: MessageSource
) {

    // ログイン画面表示
    @GetMapping(value = ["/", "/login"])
    fun index(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        return mav
    }

    // 認証失敗時（ユーザ名、またはパスワード誤り）
    @GetMapping(value = ["/login-error"])
    fun loginError(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        // ログインエラーテキストを表示させる
        mav.addObject("errorMessage",
                messageSource.getMessage("login.BAD_CREDENTIAL", null, Locale.JAPAN))
        return mav
    }

    // 認証失敗時（アカウントロック）
    @GetMapping(value = ["/account-lock"])
    fun accountLock(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        // ログインエラーテキストを表示させる
        mav.addObject("errorMessage",
                messageSource.getMessage("login.ACCOUNT_LOCK", null, Locale.JAPAN))
        return mav
    }

    // 認証失敗時（その他のエラー）
    @GetMapping(value = ["/system-error"])
    fun systemError(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        // ログインエラーテキストを表示させる
        mav.addObject("errorMessage",
                messageSource.getMessage("login.OTHER_ERROR", null, Locale.JAPAN))
        return mav
    }

    // ログアウト後のログイン画面表示
    @GetMapping(value = ["/logout"])
    fun logout(mav: ModelAndView): ModelAndView {
        mav.viewName = "login"
        mav.addObject("message", "ログアウトしました。")
        return mav
    }

    // パスワードリセット画面表示
    @GetMapping(value = ["/reset"])
    fun reset(mav: ModelAndView): ModelAndView {
        mav.viewName = "reset"
        return mav
    }

}