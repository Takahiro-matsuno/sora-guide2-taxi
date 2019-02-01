package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.util.*

@Controller
class ApplicationController(
        private val taxiRepository: TaxiInfoRepository,
        private val rsvCompService: ReservationCompleteService,
        private val rsvDetailService: ReservationDetailService,
        private val rsvChangeService: ReservationChangeService
) {

    //登録画面
    @RequestMapping("/app/registration")
    @ResponseBody
    fun registration(mav: ModelAndView): ModelAndView {
        mav.viewName = "registration"
        mav.addObject("taxiList", taxiRepository.findAll())
        mav.addObject("reservationForm", ReservationForm())

        return mav
    }

    //予約確認画面
    @RequestMapping("app/confirmation")
    @ResponseBody
    fun confirmation(mav: ModelAndView, @Validated @ModelAttribute form: ReservationForm, result: BindingResult): ModelAndView {
        //TODO 入力チェック処理（未作成）の呼び出し
        //バリデートの結果でエラーありの場合の処理（現状、フォームにバリデートの定義がないため、デッドロジック）
        if (result.hasErrors()) {
            mav.viewName = "registration"
            return mav
        }

        mav.viewName = "confirmation"
        mav.addObject("reservationForm",form)

        val taxiCompanyName = taxiRepository.findById(form.company_id).get()
        mav.addObject("taxiCompanyName", taxiCompanyName.name)
        return mav
    }

    //予約完了画面
    @RequestMapping("app/rsvComplete")
    @ResponseBody
    fun rsvComplete(mav: ModelAndView,
                    @ModelAttribute("reservationForm") rsvForm: ReservationForm): ModelAndView {
        //登録処理
        //TODO 登録エラー時の処理を追加する（try-catch）
        rsvCompService.setBooking(rsvForm)
        rsvCompService.complete()

        mav.viewName = "complete"
        return mav
    }

    //変更完了画面
    @RequestMapping("app/changeComplete")
    @ResponseBody
    fun changeComplete(mav: ModelAndView,
                       @ModelAttribute("reservationForm") rsvForm: ReservationForm,
                       @ModelAttribute("id")id:String,
                       @ModelAttribute("name")name:String): ModelAndView {
        val checkBooking = rsvDetailService.getDetail(id)
        //検索にかからない場合
        if(!checkBooking.isPresent){
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }
        //ID改ざん対策
        if(checkBooking.get().name.trim() != name.trim()){
            println("ERROR：変更前後の予約で名前が一致しない")
            println("DB->${checkBooking.get().name.trim()}")
            println("入力->${name.trim()}")
            throw Exception()
        }

        //変更処理
        rsvChangeService.change(id,rsvForm)

        //完了画面へ遷移
        mav.viewName = "complete"
        //TODO addObject
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
    fun detail(mav: ModelAndView/*,@ModelAttribute("id")id:String*/): ModelAndView {
        mav.viewName = "detail"
        val bookingInfo = rsvDetailService.getDetail("0000000002")

        mav.addObject("rsvDetail",bookingInfo)
        mav.addObject("status",rsvDetailService.statusText)
        mav.addObject("companyName",rsvDetailService.taxiCompanyName)
        return mav
    }

    //変更入力画面
    @RequestMapping("app/change")
    @ResponseBody
    fun change(mav: ModelAndView,@ModelAttribute("id")id:String): ModelAndView {
        println("送られた予約ID：$id")
        mav.viewName = "change"
        val bookingInfo = rsvDetailService.getChangeDetail(id)

        mav.addObject("reservationForm",bookingInfo)
        mav.addObject("id",id)
        return mav
    }

    //TODO WEB用のメールのリンクから予約詳細の間に挟む画面
}