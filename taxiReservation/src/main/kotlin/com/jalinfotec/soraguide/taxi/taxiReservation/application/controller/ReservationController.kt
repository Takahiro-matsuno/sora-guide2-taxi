package com.jalinfotec.soraguide.taxi.taxiReservation.application.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.utils.cookie.CookieManager
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.cookie.UserAgentManager
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.cookie.UuidManager
import com.jalinfotec.soraguide.taxi.taxiReservation.application.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.TaxiInformationService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.validation.FormValidate
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

    /**
     * 登録画面
     */
    @GetMapping("/app/registration")
    fun registration(
            mav: ModelAndView,
            request: HttpServletRequest,
            response: HttpServletResponse
    ): ModelAndView {
        mav.viewName = "registration"
        UuidManager().check(request, response)
        mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
        mav.addObject("reservationForm", setRsvForm(ReservationForm()))
        mav.addObject("isTab", tabDisplay(request))

        return mav
    }

    /**
     * 登録画面に戻る
     */
    @PostMapping("/app/registration")
    fun backRegistration(
            rsvForm: ReservationForm,
            mav: ModelAndView,
            request: HttpServletRequest
    ): ModelAndView {
        mav.viewName = "registration"
        mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
        mav.addObject("reservationForm", rsvForm)
        mav.addObject("isTab", tabDisplay(request))

        return mav
    }

    /**
     * 登録確認画面
     */
    @PostMapping("app/confirmation")
    fun confirmation(
            @Validated @ModelAttribute rsvForm: ReservationForm, result: BindingResult,
            mav: ModelAndView,
            request: HttpServletRequest
    ): ModelAndView {
        var isError = false

        //単項目チェック
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            isError = true
            mav.addObject("errorMessage", result.fieldErrors[0].defaultMessage)
        }

        val formValidateMessage = FormValidate().registrationCheck(rsvForm)

        //相関チェックと不足している単項目チェック
        if (!isError && formValidateMessage.isNotEmpty()) {
            println("メソッドの入力チェックでエラー")
            isError = true
            mav.addObject("errorMessage", formValidateMessage)
        }

        //チェックでエラーがある場合、元の画面へ戻る
        if(isError){
            val locale = CookieManager().getLocale(request)
            mav.viewName = "${locale}registration"
            mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
            mav.addObject("isTab", tabDisplay(request))
            return mav
        }

        //乗車日付を画面表示用に成形
        rsvForm.rideOnDateStr = rsvForm.rideOnDate.toString().replace("-", "/")

        //確認画面へ遷移
        val locale = CookieManager().getLocale(request)
        mav.viewName = "${locale}confirmation"
        mav.addObject("reservationForm", rsvForm)

        return mav
    }

    /**
     * フォーム初期化処理
     */
    fun setRsvForm(rsvForm: ReservationForm): ReservationForm {
        //日付設定用にカレンダークラスの変数を宣言
        val cal = Calendar.getInstance()

        //5分単位で丸める処理
        val minute = cal.get(Calendar.MINUTE)
        val addValue = 5 - (minute % 5)
        cal.add(Calendar.MINUTE, addValue)

        //Formの更新
        rsvForm.rideOnDate = Date(cal.time.time)
        rsvForm.rideOnTime = Time(cal.time.time).toString().substring(0, 5)

        return rsvForm
    }

    /**
     * タブ表示判定
     */
    fun tabDisplay(request: HttpServletRequest): Boolean {
        //スマホアプリ遷移の場合は画面上部にタブを表示
        return UserAgentManager().checkAndroidApp(request)
    }
}