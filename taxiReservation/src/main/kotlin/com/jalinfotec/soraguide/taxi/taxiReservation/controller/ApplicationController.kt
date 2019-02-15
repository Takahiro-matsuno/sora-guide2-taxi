package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationListService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 予約確認周りの画面表示用コントローラクラス
 *   ・予約一覧画面
 *   ・予約詳細画面
 *   ・変更入力画面
 *   ・予約認証画面
 */
@Controller
class ApplicationController(
        private val rsvDetailService: ReservationDetailService,
        private val rsvListService: ReservationListService
) {
    //一覧画面
    @RequestMapping("app/list")
    @ResponseBody
    fun list(mav: ModelAndView, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        mav.viewName = "list"

        val list = rsvListService.getList(request, response)
        mav.addObject("rsvList", list)

        if (list.isEmpty()) {
            mav.addObject("isEmpty", true)
        } else {
            mav.addObject("isEmpty", false)
        }
        return mav
    }

    //詳細画面
    @RequestMapping("app/detail")
    @ResponseBody
    fun detail(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        //TODO 直打ち対策
        mav.viewName = "detail"
        val bookingInfo = rsvDetailService.getDetail(id)

        mav.addObject("rsvDetail", bookingInfo.get())
        mav.addObject("status", rsvDetailService.statusText)
        mav.addObject("companyName", rsvDetailService.taxiCompanyName)
        return mav
    }

    //変更入力画面
    @RequestMapping("app/change")
    @ResponseBody
    fun change(mav: ModelAndView, @ModelAttribute("id") id: String): ModelAndView {
        //TODO 直打ち対策
        mav.viewName = "change"
        val bookingInfo = rsvDetailService.getChangeDetail(id)

        mav.addObject("reservationForm", bookingInfo)
        return mav
    }

    //予約認証画面
    @RequestMapping("app/certificateInput")
    @ResponseBody
    fun certificateInput(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        mav.viewName = "certification"
        //TODO 予約完了メールに記載のアドレスからGETで遷移の想定のため、それ以外をはじくようにする
        mav.addObject("id", id)
        mav.addObject("mail", "")

        return mav
    }

    //予約認証画面から予約詳細への遷移
    @RequestMapping("app/certificateResult")
    @ResponseBody
    fun certificateResult(mav: ModelAndView, @ModelAttribute("id") id: String,
                          @ModelAttribute("mail") mail: String): ModelAndView {

        val bookingInfo = rsvDetailService.detailCertificates(id, mail)

        return if (bookingInfo.isPresent) {
            detail(mav, id)
        } else {
            certificateInput(mav, id)
        }
    }
}