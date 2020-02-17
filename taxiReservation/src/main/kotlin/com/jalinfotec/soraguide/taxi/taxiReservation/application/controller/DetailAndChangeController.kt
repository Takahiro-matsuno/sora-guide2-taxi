package com.jalinfotec.soraguide.taxi.taxiReservation.application.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.ReservationListService
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
class DetailAndChangeController(
        private val rsvDetailService: ReservationDetailService,
        private val rsvListService: ReservationListService,
        private val rsvChangeService: ReservationChangeService
) {
    /**
     * 一覧画面
     */
    @GetMapping("app/list")
    fun list(
            mav: ModelAndView,
            request: HttpServletRequest,
            response: HttpServletResponse
    ): ModelAndView {
        mav.viewName = "list"

        val list = rsvListService.getList(request, response)
        mav.addObject("rsvList", list)

        return mav
    }

    /**
     * 詳細画面
     */
    @PostMapping("app/detail")
    fun detail(
            @RequestParam("id") id: String,
            mav: ModelAndView,
            request: HttpServletRequest
    ): ModelAndView {
        mav.viewName = "detail"
        val rsvDetail = rsvDetailService.getDetail(id, request) ?: throw Exception()

        mav.addObject("rsvDetail", rsvDetail)
        return mav
    }

    /**
     * 変更入力画面
     */
    @PostMapping("app/change")
    fun change(
            @RequestParam("id") id: String,
            mav: ModelAndView,
            request: HttpServletRequest
    ): ModelAndView {
        mav.viewName = "change"
        val changeForm = rsvChangeService.getChangeDetail(id, request) ?: throw Exception()

        mav.addObject("changeForm", changeForm)
        return mav
    }

    /**
     * 予約認証画面
     */
    @GetMapping("app/certificateInput")
    fun certificateInput(
            mav: ModelAndView,
            @RequestParam("id") id: String
    ): ModelAndView {
        mav.viewName = "certification"
        mav.addObject("id", id)

        return mav
    }

    /**
     * 予約認証後処理
     */
    @PostMapping("app/certificateResult")
    fun certificateResult(
            @RequestParam("id") id: String,
            @RequestParam("mail") mail: String,
            mav: ModelAndView,
            request: HttpServletRequest,
            response: HttpServletResponse
    ): ModelAndView {

        val rsvDetail = rsvDetailService.detailCertificates(id, mail, request, response)

        return if (rsvDetail != null) {
            mav.viewName = "detail"
            mav.addObject("rsvDetail", rsvDetail)
            mav
        } else {
            mav.addObject("errorMessage", "予約番号、またはメールアドレスに誤りがあります。")
            certificateInput(mav, id)
        }
    }
}