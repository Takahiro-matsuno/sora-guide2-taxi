package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationListService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
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
    // TODO 一覧表示フォームを作る
    //一覧画面
    @GetMapping("app/list")
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

    // TODO NOT BOOKING
    // TODO
    //詳細画面
    @PostMapping("app/detail")
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
    @PostMapping("app/change")
    fun change(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        //TODO 直打ち対策
        mav.viewName = "change"
        val bookingInfo = rsvDetailService.getChangeDetail(id)

        mav.addObject("reservationForm", bookingInfo)
        return mav
    }
    //予約認証画面
    @GetMapping("app/certificateInput")
    fun certificateInput(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        mav.viewName = "certification"
        mav.addObject("id", id)
        mav.addObject("mail", "")

        return mav
    }

    //予約認証画面から予約詳細への遷移
    @PostMapping("app/certificateResult")
    fun certificateResult(mav: ModelAndView, @RequestParam("id") id: String,
                          @RequestParam("mail") mail: String): ModelAndView {

        val bookingInfo = rsvDetailService.detailCertificates(id, mail)

        return if (bookingInfo.isPresent) {
            detail(mav, id)
        } else {
            certificateInput(mav, id)
        }
    }
}