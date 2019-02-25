package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
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
        private val rsvListService: ReservationListService,
        private val rsvChangeService: ReservationChangeService
) {
    //一覧画面
    @GetMapping("app/list")
    fun list(mav: ModelAndView, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        mav.viewName = "list"

        val list = rsvListService.getList(request, response)
        mav.addObject("rsvList", list)
        mav.addObject("isEmpty", list.isEmpty())

        return mav
    }

    //詳細画面
    @PostMapping("app/detail")
    fun detail(mav: ModelAndView,
               @RequestParam("id") id: String,
               request: HttpServletRequest): ModelAndView {
        mav.viewName = "detail"
        val rsvDetail = rsvDetailService.getDetail(id, request) ?: throw Exception()

        mav.addObject("rsvDetail", rsvDetail)
        return mav
    }

    //変更入力画面
    @PostMapping("app/change")
    fun change(mav: ModelAndView,
               @RequestParam("id") id: String,
               request: HttpServletRequest): ModelAndView {
        mav.viewName = "change"
        val rsvInfo = rsvChangeService.getChangeDetail(id, request)

        mav.addObject("reservationForm", rsvInfo)
        return mav
    }

    //予約認証画面
    @GetMapping("app/certificateInput")
    fun certificateInput(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        mav.viewName = "certification"
        mav.addObject("reservationId", id)
        mav.addObject("mail", "")

        return mav
    }

    //予約認証画面から予約詳細への遷移
    @PostMapping("app/certificateResult")
    fun certificateResult(mav: ModelAndView, @RequestParam("id") id: String,
                          @RequestParam("mail") mail: String,
                          request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        val rsvDetail = rsvDetailService.detailCertificates(id, mail, request, response)

        return if (rsvDetail != null) {
            mav.viewName = "detail"
            mav.addObject("rsvDetail", rsvDetail)
            mav
        } else {
            mav.addObject("errorMassage","予約番号、またはメールアドレスに誤りがあります。")
            certificateInput(mav, id)
        }
    }
}