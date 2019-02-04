package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

@Controller
class ApplicationController(
        private val taxiRepository: TaxiInfoRepository,
        private val bookingRepository:BookingInfoRepository,
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
        mav.addObject("reservationForm", form)

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
        rsvCompService.complete(rsvForm)

        mav.viewName = "complete"
        return mav
    }

    //変更完了画面
    @RequestMapping("app/changeComplete")
    @ResponseBody
    fun changeComplete(mav: ModelAndView,
                       @ModelAttribute("reservationForm") rsvForm: ReservationForm,
                       @ModelAttribute("id") id: String,
                       @ModelAttribute("name") name: String): ModelAndView {
        //変更処理
        rsvChangeService.change(id, rsvForm, name)

        //TODO 予約情報の取得処理？変更処理からリターンではなく、再度予約情報取得処理を回す方が良いが。。画面表示次第。

        //完了画面へ遷移
        mav.viewName = "complete"
        //TODO addObject
        mav.addObject("title", "変更完了")
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

        //TODO 一旦全部取得
        mav.addObject("rsvList",bookingRepository.findAll(Sort(Sort.Direction.ASC,"id")))
        return mav
    }

    //詳細画面
    @RequestMapping("app/detail")
    @ResponseBody
    fun detail(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
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
        mav.viewName = "change"
        val bookingInfo = rsvDetailService.getChangeDetail(id)

        mav.addObject("reservationForm", bookingInfo)
        mav.addObject("id", id)
        return mav
    }

    @RequestMapping("app/certificateInput")
    @ResponseBody
    fun certificateInput(mav: ModelAndView, @ModelAttribute("id") id: String): ModelAndView {
        mav.viewName = "certification"
        mav.addObject("id", id)
        mav.addObject("mail", "")

        return mav
    }

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