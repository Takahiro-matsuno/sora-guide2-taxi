package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.user

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.UserSettingForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import com.microsoft.applicationinsights.TelemetryClient
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

        val telemetry = TelemetryClient()
        // アカウント取得
        return if (userService.findByCompanyIdAndUsername(user.getCompanyId(), user.username)) {
            mav.viewName = "contents/userSetting"
            mav.addObject("usForm", UserSettingForm(username = user.username))
            mav
        } else {
            // アカウントが見つからない場合はエラー
            // TODO エラー
            telemetry.trackEvent("UserNotFound")
            mav.viewName = "contents/error"
            //mav.addObject("", "ユーザーが見つかりません。")
            mav
        }
    }

    // TODO パスワードチェックをJSかサーバー側で実装する
    // パスワード変更
    @PostMapping(value = ["/user/setting"])
    fun changePassword(
            @AuthenticationPrincipal user: UserAccount,
            @ModelAttribute(value = "usForm") usForm: UserSettingForm,
            mav: ModelAndView
    ): ModelAndView {
        val telemetry = TelemetryClient()

        if (user.username != usForm.username) {
            // TODO エラー
            // ユーザー名が一致しない場合はエラー
            telemetry.trackEvent("UserInfo_change_failure:username_mismatch")
            mav.viewName = "contents/error"
            mav.addObject("message", "再度ログインしてください。")
            return mav
        }

        // ユーザー情報更新処理
        mav.viewName = "/contents/userSetting"
        if (userService.changePassword(user, usForm)) {
            // 更新成功
            telemetry.trackEvent("UserInfo_change_successful")
            mav.addObject("message", "ユーザ情報を更新しました。")
            mav.addObject("usForm", UserSettingForm(username = user.username))
        } else {
            // 更新失敗
            telemetry.trackEvent("UserInfo_change_failure")
            mav.addObject("message", "更新に失敗しました。")
        }
        return mav
    }


    // パスワード初期化
    @PostMapping(value = ["/reset"])
    fun doReset(@RequestParam userName: String, @RequestParam("mail") inputMail: String, mav: ModelAndView): ModelAndView {
        var result: Int
        val telemetry = TelemetryClient()

        telemetry.trackTrace("user:${userName},mail:${inputMail}")
        try {
            result = userService.resetPassword(userName, inputMail)
        } catch (e: Exception) {
            telemetry.trackException(e)
            result = 1
        }

        if (result == 0) {
            telemetry.trackEvent("password_reset_successful")
            mav.viewName = "login"
            mav.addObject("message", "パスワードをリセットしました。メールをご確認ください。")
        } else if (result == 1) {
            telemetry.trackEvent("passwords_reset_failure")
            mav.viewName = "reset"
            mav.addObject("errorMessage", "ユーザー名または、メールアドレスに誤りがあります。")
        }

        return mav
    }
}