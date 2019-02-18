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

    // 予約一覧画面
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
            // TODO データ取得なし : "予約はありません。"
            // TODO DBエラー : "データベースエラー"
            mav.addObject("isEmpty", true)
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

    /* TODO 更新時のエラー
     * エラーメッセージ
     * 　There was an unexpected error (type=Bad Request, status=400).
     * 　Missing request attribute 'rsvForm' of type ReservationForm
     *　RequestBody
     *   _csrf: 0aae4cca-85b8-44d4-af26-e28360baff81 // TODO ②　①を変更してもダメな場合は"_csrf"について調査
     *   statusName: キャンセル済み
     *   rideOnDate: 2019-02-15
     *   rideOnTime: 18:29:00 // TODO ① Time型の変換
     *   adult: 1
     *   child: 0
     *   carDispatchNumber: 1
     *   destination: hoge
     *   passengerName: hoge
     *   passengerContact: hoge
     *   passengerMail: 0@0
     *   comment: hofe
     *   carNumber:
     *   carContact:
     *   notice:
     * 　
     */
    // 予約更新処理
    @PostMapping(value = ["/reservation/update"])
    fun update(
            @AuthenticationPrincipal user: UserAccount,
            @RequestAttribute(value = "rsvForm") rsvForm: ReservationForm
    ): String {


        return if (reservationService.updateDetail(user.getCompanyId(), rsvForm)) {
            println("更新成功")
            // 予約一覧にフォワードする
            "forward:/contents/reservationList"
        } else {
            println("更新失敗")
            "forward:/contents/error"
        }
    }
}