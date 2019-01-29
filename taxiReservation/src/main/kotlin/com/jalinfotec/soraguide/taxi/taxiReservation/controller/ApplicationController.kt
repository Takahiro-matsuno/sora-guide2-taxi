package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

class ApplicationController {
    @Autowired
    var taxiRepository: TaxiInfoRepository? = null

    //登録画面
    @RequestMapping("app/registration")
    @ResponseBody
    fun registration(mav: ModelAndView): ModelAndView {
        mav.viewName = "registration"
        mav.addObject("taxiList",taxiRepository?.findAll())
        return mav
    }

    //予約確認画面
    @RequestMapping("app/confirmation")
    @ResponseBody
    fun confirmation(mav: ModelAndView): ModelAndView {
        //TODO 入力チェック処理（未作成）の呼び出し
        mav.viewName = "confirmation"
        return mav
    }

    //予約完了画面
    @RequestMapping("app/rsvComplete")
    @ResponseBody
    fun rsvComplete(mav: ModelAndView,
                    @RequestParam("rsvForm")rsvForm :ReservationForm): ModelAndView {
        //登録処理
        //TODO エラー時の処理を追加する（try-catch）
        val rsvCompService = ReservationCompleteService()
        rsvCompService.setBooking(rsvForm)
        rsvCompService.complete()

        mav.viewName = "complete"
        return mav
    }

    //変更完了画面
    @RequestMapping("app/changeComplete")
    @ResponseBody
    fun changeComplete(mav: ModelAndView): ModelAndView {
        mav.viewName = "complete"
        return mav
    }

    //取消完了画面
    @RequestMapping("app/deleteComplete")
    @ResponseBody
    fun deleteComplete(mav: ModelAndView): ModelAndView {
        mav.viewName = "complete"
        return mav
    }

    //一覧画面
    @RequestMapping("app/list")
    @ResponseBody
    fun list(mav: ModelAndView): ModelAndView {
        mav.viewName = "list"
        return mav
    }

    //詳細画面
    @RequestMapping("app/detail")
    @ResponseBody
    fun detail(mav: ModelAndView): ModelAndView {
        mav.viewName = "detail"
        return mav
    }

    //変更入力画面
    @RequestMapping("app/change")
    @ResponseBody
    fun change(mav: ModelAndView): ModelAndView {
        mav.viewName = "change"
        return mav
    }

    //TODO WEB用のメールのリンクから予約詳細の間に挟む画面
}