package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.company

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.TaxiCompanyForm
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class CompanyController {

    // 会社情報画面の表示
    @GetMapping(value = ["/company/setting"])
    fun getCompany(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {
        mav.viewName = "contents/companySetting"
        return mav
    }

    // 会社情報の更新
    // 会社情報画面の表示
    @PostMapping(value = ["company/setting"])
    fun updateCompanyInfo(
            @AuthenticationPrincipal user: UserAccount,
            @ModelAttribute taxiForm: TaxiCompanyForm,
            mav: ModelAndView
    ): ModelAndView {
        // TODO 処理を考える

        mav.viewName = "contents/companySetting"
        if (false) {
            // 会社情報の更新に失敗した場合
            mav.addObject("", "更新に失敗しました。")
        }

        if (false) {
            // 会社情報の取得に失敗した場合
            mav.viewName = "contents/error"
            mav.addObject("", "データベースエラー")
            return mav
        }
        // Viewに会社情報を設定
        //mav.addObject("", "")
        return mav
    }

}