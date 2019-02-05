package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.reservation

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.ReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class ReservationController {

    @Autowired
    private lateinit var reservationService: ReservationService

    // 予約一覧画面
    @RequestMapping(value = ["reservation/list"])
    fun getReservationList(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/reservationList"

        // ログインユーザーの会社IDを取得する
        val companyId = user.getCompanyId()
        println("${user.username}: $companyId")

        // 予約テーブルから会社IDで検索をかけてデータを取得する
        val rsvList = reservationService.getListDefault(companyId)

        if (rsvList.any()) {
            // リスト表示のために予約情報一覧を渡す
            println("予約あり")
            mav.addObject("rsvList", rsvList)
        } else {
            println("予約なし")
            mav.addObject("isEmpty", true)
        }
        return mav
    }


    // 予約詳細画面
    @RequestMapping(value = ["/reservation/detail"], method = [RequestMethod.GET])
    fun getReservationDetail(
            @AuthenticationPrincipal user: UserAccount,
            @RequestParam(value = "reservationId", required = true) reservationId: String,
            mav: ModelAndView
    ): ModelAndView {

        // 認証ユーザーから会社IDを取得する
        val companyId = user.getCompanyId()
        //
        val reservation = reservationService.getDetail(companyId, reservationId)

        if (reservation == null) {
            mav.viewName = "contents/error"
            return mav
        }
        mav.viewName = "contents/reservationDetail"
        //mav.addObject("reservation", reservation)
        return mav
    }

    // 更新エラー画面
    @RequestMapping(value = ["/reservation/update-error"], method = [RequestMethod.GET])
    fun updateError(mav: ModelAndView): ModelAndView {
        mav.viewName = "error"
        mav.addObject("errorMessage", "")
        return mav
    }

    // 検索処理
    @RequestMapping(value = ["/reservation/search"], method = [RequestMethod.GET])
    fun search(
            @AuthenticationPrincipal user: UserAccount,
            @RequestParam(value = "reservationId", required = true) reservationId: String,
            mav: ModelAndView
    ): ModelAndView {
        // 詳細画面を表示する
        mav.viewName = "reservationDetail"
        return mav
    }

    // 更新処理
    @RequestMapping(value = ["/reservation/update"], method = [RequestMethod.POST])
    fun update(): String {
        /*
        return if (reservationService.update("")) { // 更新に失敗した場合は更新エラー画面表示
            "forward:/reservation/update-error"
        } else { // 更新に成功した場合は一覧画面に戻る
            "forward:/reservationList"
        }
        */
        return "forward:/user"
    }
}