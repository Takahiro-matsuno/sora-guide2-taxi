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


    /** TODO パスワードの更新できないエラーを解決する
     * UserAccountServiceでフォーム(暗号化済み)のパスワードとDBのパスワード(暗号化済み)が一致しないエラー
     * - 同じ文字列でも毎回異なる結果に暗号化されるため、パスワードが一致しない
     * - 現在のユーザー取得はユーザー名のみでの検索しかないため、ユーザー名＋パスワードでの取得をする
     * - UserDetailsServiceではなく以下を使う
     *  https://qiita.com/pale2f/items/3fb28e76f969d7c18f06
     */
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
            mav.addObject("usForm", UserSettingForm(username = user.username))
        } else {
            // 更新失敗
            mav.addObject("message","更新に失敗しました。")
        }
        return mav
    }
}