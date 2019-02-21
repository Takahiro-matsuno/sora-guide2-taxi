package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.reservation

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.ReservationService
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class ReservationController(
        private val reservationService: ReservationService
) {

    // 予約一覧画面の表示
    @GetMapping(value = ["reservation/list"])
    fun getReservationList(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        // 認証ユーザーの会社IDをキーに予約情報フォームを取得
        val rsvFormList =reservationService.getListDefault(user.getCompanyId())

        mav.viewName = "contents/reservationList"
        if (rsvFormList.any()) {
            // Viewに取得結果を設定
            mav.addObject("rsvFormList", rsvFormList)
        } else {
            // TODO 0件の場合の挙動を確認する
            // TODO DBエラー : "データベースエラー"
            // 予約一覧取得失敗時
            // mav.addObject("", "予約情報がありません。")
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

    // TODO ModelAttribute と RequestAttributeの違いを調べる
    // 予約情報の更新
    // 予約一覧表示（予約詳細からのデータ更新）
    @PostMapping(value = ["/reservation/list"])
    fun updateReservation(
            @AuthenticationPrincipal user: UserAccount,
            @ModelAttribute(value = "rsvForm") rsvForm: ReservationForm,
            mav: ModelAndView
    ): ModelAndView {

        val companyId = user.getCompanyId()
        mav.viewName = "contents/reservationList"

        // 予約情報更新処理
        if (!reservationService.updateDetail(companyId, rsvForm)) {
            // 更新エラー表示をViewに追加
            // TODO 処理未チェック
            mav.addObject("errorMessage", "更新に失敗しました")
        }

        // 予約一覧の取得
        val rsvFormList = reservationService.getListDefault(companyId)

        if (rsvFormList.any()) {
            mav.addObject("rsvFormList", rsvFormList)
        } else {
            // 予約一覧取得失敗時
            // mav.addObject("", "予約情報がありません。")
        }
        return mav
    }
}