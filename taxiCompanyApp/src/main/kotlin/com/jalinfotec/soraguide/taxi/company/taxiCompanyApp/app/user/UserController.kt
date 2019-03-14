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

        // アカウント取得
        return if (userService.findByCompanyIdAndUsername(user.getCompanyId(), user.username)) {
            mav.viewName = "contents/userSetting"
            mav.addObject("usForm", UserSettingForm(username = user.username))
            mav
        } else {
            // アカウントが見つからない場合はエラー
            // TODO エラー
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

        if (user.username != usForm.username) {
            // TODO エラー
            // ユーザー名が一致しない場合はエラー
            mav.viewName = "contents/error"
            mav.addObject("message", "再度ログインしてください。")
            return mav
        }

        // ユーザー情報更新処理
        mav.viewName = "/contents/userSetting"
        if (userService.changePassword(user, usForm)) {
            // 更新成功
            mav.addObject("message","ユーザ情報を更新しました。")
            mav.addObject("usForm", UserSettingForm(username = user.username))
        } else {
            // 更新失敗
            mav.addObject("message","更新に失敗しました。")
        }
        return mav
    }


    // パスワード初期化
    @PostMapping(value = ["/reset"])
    fun doReset(@RequestParam userName: String, @RequestParam("mail") inputMail: String, mav: ModelAndView): ModelAndView {
        userService.resetPassword(userName,inputMail)

        mav.viewName = "login"
        mav.addObject("message", "パスワードをリセットしました。メールをご確認ください。")

        return mav
    }
}