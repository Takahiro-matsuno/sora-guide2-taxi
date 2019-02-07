package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.reservation

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.ReservationService
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.MetaData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class ReservationController {

    @Autowired
    private lateinit var reservationService: ReservationService

    @RequestMapping(value = ["reservagtion/error"])
    fun error(mav: ModelAndView): ModelAndView {
        mav.viewName = "contents/error"
        return mav
    }
    //
    // 予約一覧画面
    @RequestMapping(value = ["reservation/list"])
    fun getReservationList(
            @AuthenticationPrincipal user: UserAccount,
            mav: ModelAndView
    ): ModelAndView {

        mav.viewName = "contents/reservationList"

        // 認証ユーザーから会社IDを取得する
        val companyId = user.getCompanyId()

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

    // 予約詳細画面の表示
    @RequestMapping(value = ["/reservation/detail"], method = [RequestMethod.GET])
    fun getReservationDetail(
            @AuthenticationPrincipal user: UserAccount,
            @RequestParam(value = "reservationId", required = true) reservationId: String,
            mav: ModelAndView
    ): ModelAndView {

        // 認証ユーザーから会社IDを取得する
        val companyId = user.getCompanyId()

        // 予約テーブルから会社IDと予約番号で検索をかけてデータを取得する
        val rsvInfo = reservationService.getDetail(companyId, reservationId)

        if (rsvInfo == null) {
            // 予約情報がない場合はエラー画面を表示する
            mav.viewName = "contents/error"
            return mav
        }
        mav.viewName = "contents/reservationDetail"
        mav.addObject("rsvInfo", rsvInfo)
        //mav.addObject("status_list", MetaData.STATUS_LIST)
        mav.addObject("pax_range", MetaData.PAX_RANGE)
        return mav
    }

    // TODO 更新方法は予約サイトを参考にする
    // 更新処理
    @RequestMapping(value = ["/reservation/update"], method = [RequestMethod.POST])
    fun update(
            @RequestAttribute(value = "rsvInfo")rsvForm: ReservationForm

    ): String {
        /*
        return if (reservationService.updateDetail(rsvInfo)) { // 更新成功
            println("更新成功")
            "forward:/reservation/list"
        } else { // 更新失敗
            println("更新失敗")
            "forward:/reservation/error"
        }
        */
        return "forward:/reservation/list"
    }

}