package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView

/**
 * 登録フローの画面表示用コントローラクラス
 *   ・登録画面
 *   ・登録確認画面
 */
@Controller
class ReservationController(
        private val taxiRepository: TaxiInfoRepository
) {

    //登録画面
    @RequestMapping("/app/registration")
    @ResponseBody
    fun registration(mav: ModelAndView): ModelAndView {
        //TODO アプリ遷移かWeb遷移かを判定して表示項目の出し分け
        mav.viewName = "registration"
        mav.addObject("taxiList", taxiRepository.findAll())
        mav.addObject("reservationForm", ReservationForm())

        return mav
    }

    //登録確認画面
    @RequestMapping("app/confirmation")
    @ResponseBody
    fun confirmation(mav: ModelAndView, @Validated @ModelAttribute rsvForm: ReservationForm, result: BindingResult): ModelAndView {
        //TODO エラー時の画面表示
        //バリデートエラー
        if (result.hasErrors()) {
            mav.viewName = "registration"
            return mav
        }
        //メール入力エラー
        if (rsvForm.mail != rsvForm.mailCheck) {
            mav.viewName = "registration"
            return mav
        }

        //確認画面へ遷移
        mav.viewName = "confirmation"
        mav.addObject("reservationForm", rsvForm)

        //タクシー会社の表示用のオブジェクトを設置
        val taxiInformation = taxiRepository.findById(rsvForm.company_id).get()
        mav.addObject("taxiCompanyName", taxiInformation.company_name)
        return mav
    }

}