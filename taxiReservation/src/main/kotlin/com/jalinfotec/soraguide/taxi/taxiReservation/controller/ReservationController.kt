package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.UserAgentManager
import com.jalinfotec.soraguide.taxi.taxiReservation.cookie.UuidManager
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.TaxiInformationService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.validation.FormValidate
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import java.sql.Date
import java.sql.Time
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


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
    fun registration(mav: ModelAndView, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        mav.viewName = "registration"

        //スマホアプリ遷移の場合は画面上部にタブを表示
        var isTabDisplay = false
        if (UserAgentManager().checkAndroidApp(request)) {
            isTabDisplay = true
        }

        UuidManager().check(request, response)

        mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
        mav.addObject("reservationForm", setRsvForm(ReservationForm()))
        mav.addObject("isTab", isTabDisplay)

        return mav
    }

    @PostMapping("/app/registration")
    fun backRegistration(mav: ModelAndView, rsvForm: ReservationForm, request: HttpServletRequest): ModelAndView {
        mav.viewName = "registration"

        //スマホアプリ遷移の場合は画面上部にタブを表示
        var isTabDisplay = false
        if (UserAgentManager().checkAndroidApp(request)) {
            isTabDisplay = true
        }

        mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
        mav.addObject("reservationForm", rsvForm)
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

        //5分単位で丸める処理
        val minute = cal.get(Calendar.MINUTE)
        val addValue = 5 - (minute % 5)
        cal.add(Calendar.MINUTE, addValue)

        // 日本標準時に合わせる
        cal.add(Calendar.HOUR_OF_DAY, 9)

        println(cal.time.toString())

        //Formの更新
        rsvForm.date = Date(cal.time.time)
        rsvForm.time = Time(cal.time.time).toString().substring(0, 5)

        return rsvForm
    }
}