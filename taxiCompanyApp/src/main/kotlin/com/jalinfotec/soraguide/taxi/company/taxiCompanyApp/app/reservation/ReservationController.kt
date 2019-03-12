package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.reservation

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationSearchForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.ReservationService
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import org.springframework.context.MessageSource
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import java.util.regex.Pattern

@Controller
class ReservationController(
        private val reservationService: ReservationService,
        private val messageSource: MessageSource
) {

    // 予約一覧画面の表示
    @GetMapping(value = ["reservation/list"])
    fun getReservationList(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        // 認証ユーザーの会社IDをキーに予約情報フォームを取得
        val rsvFormList = reservationService.getListDefault(user.getCompanyId())

        // 選択可能な予約ステータス一覧
        mav.addObject("statusList", reservationService.getStatusList())

        // 検索フォームの初期値設定
        mav.addObject("searchForm", ReservationSearchForm())

        mav.viewName = "contents/reservationList"
        if (rsvFormList.any()) {
            // Viewに取得結果を設定
            mav.addObject("rsvFormList", rsvFormList)
        } else {
            // 予約一覧取得失敗時
            mav.addObject("errorMessage", "予約情報がありません。")
        }
        return mav
    }

    // 予約詳細画面の表示
    @GetMapping(value = ["/reservation/detail"])
    fun getReservationDetail(
            @AuthenticationPrincipal user: UserAccount,
            @RequestParam(value = "reservationId") reservationId: String,
            mav: ModelAndView
    ): ModelAndView {

        // 認証ユーザーの会社ID、予約番号をキーに予約情報を取得
        val result = reservationService.getDetail(user.getCompanyId(), reservationId)

        return if (result == null) {
            // 予約情報がない場合はエラー画面を表示する
            mav.viewName = "contents/error"
            mav
        } else {
            mav.viewName = "contents/reservationDetail"
            // Viewに取得結果を設定
            mav.addObject("rsvForm", result.first)      // 予約情報
            mav.addObject("statusList", result.second)  // 選択可能な予約ステータス一覧
            mav.addObject("paxRange", Constants.PAX_RANGE)  // 人数選択の最大値
            return mav
        }
    }

    // 予約情報の更新
    // 予約一覧表示（予約詳細からのデータ更新）
    @PostMapping(value = ["/reservation/list"])
    fun updateReservation(
            @AuthenticationPrincipal user: UserAccount,
            @Validated @ModelAttribute(value = "rsvForm") rsvForm: ReservationForm,
            result: BindingResult,
            mav: ModelAndView
    ): ModelAndView {

        val validateMessage = validate(rsvForm, result, mav)
        if (!validateMessage.isNullOrEmpty()) {
            mav.viewName = "contents/reservationDetail"
            mav.addObject("statusList", reservationService.getStatusList())  // 選択可能な予約ステータス一覧
            mav.addObject("paxRange", Constants.PAX_RANGE)  // 人数選択の最大値
            mav.addObject("errorMessage", validateMessage)  // エラーメッセージ
            return mav
        }

        val companyId = user.getCompanyId()
        mav.viewName = "contents/reservationList"

        // 選択可能な予約ステータス一覧
        mav.addObject("statusList", reservationService.getStatusList())

        // 検索フォームの初期値設定
        mav.addObject("searchForm", ReservationSearchForm())

        // 予約情報更新処理
        if (!reservationService.updateDetail(companyId, rsvForm)) {
            // 更新エラー表示をViewに追加
            mav.addObject("errorMessage", "更新に失敗しました")
        }

        // 予約一覧の取得
        val rsvFormList = reservationService.getListDefault(companyId)

        if (rsvFormList.any()) {
            mav.addObject("rsvFormList", rsvFormList)
        } else {
            // 予約一覧取得失敗時
            mav.addObject("errorMessage", "予約情報がありません。")
        }
        return mav
    }

    /**
     * 予約変更時のバリデーションチェック
     */
    fun validate(rsvForm: ReservationForm, result: BindingResult, mav: ModelAndView): String? {
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            val fieldName = messageSource.getMessage(result.fieldErrors[0].field, null, Locale.JAPAN)
            val errorMessage = result.fieldErrors[0].defaultMessage
            return "$fieldName：$errorMessage"
        }

        if (rsvForm.adult + rsvForm.child <= 0) {
            println("最低人数以下")
            return "乗車人数は1人以上で指定してください。"
        }

        val pattern = Pattern.compile("^(([0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]"
                + "+(\\.[0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]+)*)|(\"[^\"]*\"))"
                + "@[0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]+"
                + "(\\.[0-9a-zA-Z!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\}\\|~]+)*$")

        if (!pattern.matcher(rsvForm.passengerMail).find()) {
            println("メールアドレスの形式不正")
            return "メールアドレスの形式が正しくありません。"
        }

        return null
    }

    // 予約一覧画面から予約検索実行
    @PostMapping(value = ["/reservation/search"])
    fun searchReservation(
            @AuthenticationPrincipal user: UserAccount,
            @ModelAttribute(value = "searchForm") searchForm: ReservationSearchForm,
            mav: ModelAndView
    ): ModelAndView {
        // 認証ユーザーの会社IDをキーに予約情報フォームを取得
        val rsvFormList = reservationService.getListSearch(user.getCompanyId(), searchForm)

        // 選択可能な予約ステータス一覧
        mav.addObject("statusList", reservationService.getStatusList())

        // 検索フォームの初期値設定
        mav.addObject("searchForm", searchForm)

        mav.viewName = "contents/reservationList"
        if (rsvFormList.any()) {
            // Viewに取得結果を設定
            mav.addObject("rsvFormList", rsvFormList)
        } else {
            // 予約一覧取得失敗時
            mav.addObject("errorMessage", "予約情報がありません。")
        }
        return mav
    }
}