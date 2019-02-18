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

        val rsvFormList =reservationService.getListDefault(user.getCompanyId())

        mav.viewName = "contents/reservationList"
        if (rsvFormList.any()) {
            // リスト表示のために予約情報一覧を渡す
            mav.addObject("rsvFormList", rsvFormList)
        } else {
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

        // 認証ユーザーから会社IDを取得する
        val companyId = user.getCompanyId()

        // 予約テーブルから会社IDと予約番号で検索をかけてデータを取得する
        val result = reservationService.getDetail(companyId, reservationId)


        return if (result == null) {
            // 予約情報がない場合はエラー画面を表示する
            mav.viewName = "contents/error"
            mav
        } else {
            mav.viewName = "contents/reservationDetail"
            mav.addObject("rsvForm", result.first)
            mav.addObject("statusList", result.second)
            //mav.addObject("status_list", MetaData.STATUS_LIST)
            mav.addObject("paxRange", Constants.PAX_RANGE)
            return mav
        }

    }

    // TODO 更新方法は予約サイトを参考にする
    // 更新処理
    @PutMapping(value = ["/reservation/update"])
    fun update(
            @RequestAttribute(value = "rsvForm") rsvForm: ReservationForm

    ): String {

        return if (reservationService.updateDetail(rsvForm)) {
            println("更新成功")
            "forward:/reservation/list"
        } else {
            println("更新失敗")
            "forward:/reservation/error"
        }
    }
}