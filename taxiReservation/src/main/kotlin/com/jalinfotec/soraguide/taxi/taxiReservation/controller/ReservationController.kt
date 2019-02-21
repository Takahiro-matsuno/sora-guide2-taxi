package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.TaxiInformationService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.validation.FormValidate
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.sql.Date
import java.sql.Time
import java.util.Calendar
import javax.servlet.http.HttpServletRequest


/**
 * 登録フローの画面表示用コントローラクラス
 *   ・登録画面
 *   ・登録確認画面
 */
@Controller
class ReservationController(
        private val taxiInformationService: TaxiInformationService
) {

    //登録画面
    @GetMapping("/app/registration")
    fun registration(mav: ModelAndView, request: HttpServletRequest): ModelAndView {
        mav.viewName = "registration"

        //スマホアプリ遷移の場合は画面上部にタブを表示
        val userAgent = request.getHeader("user-agent")
        var isTabDisplay = false
        if (userAgent.indexOf("sora-GuideApp") > 0) {
            isTabDisplay = true
        }

        mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
        mav.addObject("reservationForm", setRsvForm(ReservationForm()))
        mav.addObject("isTab", isTabDisplay)

        return mav
    }

    @PostMapping("/app/registration")
    fun backRegistration(mav: ModelAndView, request: HttpServletRequest): ModelAndView {
        mav.viewName = "registration"

        //スマホアプリ遷移の場合は画面上部にタブを表示
        val userAgent = request.getHeader("user-agent")
        var isTabDisplay = false
        if (userAgent.indexOf("sora-GuideApp") > 0) {
            isTabDisplay = true
        }

        mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
        mav.addObject("reservationForm", setRsvForm(ReservationForm()))
        mav.addObject("isTab", isTabDisplay)

        return mav
    }

    //登録確認画面
    @PostMapping("app/confirmation")
    fun confirmation(mav: ModelAndView, @Validated @ModelAttribute rsvForm: ReservationForm, result: BindingResult): ModelAndView {
        //単項目チェック
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            val messageList = result.fieldErrors
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
            mav.addObject("errorMassage", messageList[0].defaultMessage)
            return mav
        }

        val rsvFormValidate = FormValidate()
        val formValidateMessage = rsvFormValidate.registrationCheck(rsvForm)

        //相関チェックと不足している単項目チェック
        if (formValidateMessage.isNotEmpty()) {
            println("メソッドの入力チェックでエラー")
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
            mav.addObject("errorMassage", formValidateMessage)
            return mav
        }

        //確認画面へ遷移
        mav.viewName = "confirmation"
        mav.addObject("reservationForm", rsvForm)

        return mav
    }

    //ReservationFormの初期化処理
    //基本的な初期化はフォームクラスで行うが、日付と時間は再設定する
    fun setRsvForm(rsvForm: ReservationForm): ReservationForm {
        //日付設定用にカレンダークラスの変数を宣言
        val cal = Calendar.getInstance()

        //Formから初期値（現在時刻）を取得
        cal.time = rsvForm.date

        //5分単位で丸める処理
        val minute = cal.get(Calendar.MINUTE)
        val addValue = 5 - (minute % 5)
        cal.add(Calendar.MINUTE, addValue)

        //Formの更新
        rsvForm.date = Date(cal.timeInMillis)
        rsvForm.time = Time(rsvForm.date.time).toString().substring(0, 5)

        return rsvForm
    }
}