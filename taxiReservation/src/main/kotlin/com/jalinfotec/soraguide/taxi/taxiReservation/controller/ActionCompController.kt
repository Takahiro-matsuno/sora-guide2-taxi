package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.TaxiInformationService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.validation.FormValidate
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 各種完了画面の表示用コントローラクラス
 *   ・登録完了
 *   ・変更完了
 *   ・取消完了
 */
@Controller
class ActionCompController(
        private val rsvCompService: ReservationCompleteService,
        private val rsvDetailService: ReservationDetailService,
        private val rsvChangeService: ReservationChangeService,
        private val taxiInformationService: TaxiInformationService
) {

    enum class ActionType { ADD, CHANGE, CANCEL }

    //登録完了画面
    @PostMapping("app/rsvComplete")
    fun rsvComplete(mav: ModelAndView,
                    @Validated @ModelAttribute("reservationForm") rsvForm: ReservationForm,
                    result: BindingResult,
                    request: HttpServletRequest): ModelAndView {

        //単項目チェック
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            val messageList = result.fieldErrors
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
            mav.addObject("errorMassage", messageList[0].defaultMessage)
            return mav
        }

        //相関チェックと不足している単項目チェック
        val formValidateMessage = FormValidate().confirmCheck(rsvForm)

        if (formValidateMessage.isNotEmpty()) {
            println("メソッドの入力チェックでエラー")
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiInformationService.getTaxiNameList())
            return mav
        }

        //登録処理
        var rsvId = ""
        try {
            rsvId = rsvCompService.complete(rsvForm, request)
        } catch (e: Exception) {
            mav.viewName = "confirmation"
            mav.addObject("reservationForm", rsvForm)
        }

        return completeTransition(mav, rsvId, ActionType.ADD, request)
    }

    //変更完了画面
    @PostMapping("app/changeComplete")
    fun changeComplete(mav: ModelAndView,
                       @Validated @ModelAttribute("reservationForm") rsvForm: ChangeForm,
                       result: BindingResult, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        //単項目チェック
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            val messageList = result.fieldErrors
            mav.viewName = "change"
            mav.addObject("errorMassage", messageList[0].defaultMessage)
            return mav
        }

        //相関チェックと不足している単項目チェック
        val formValidateMessage = FormValidate().changeCheck(rsvForm)

        if (formValidateMessage.isNotEmpty()) {
            println("メソッドの入力チェックでエラー")
            mav.viewName = "change"
            mav.addObject("errorMassage", formValidateMessage)
            return mav
        }

        //変更処理
        val rsvId: String
        try {
            rsvId = rsvChangeService.change(rsvForm, request)
        } catch (e: Exception) {
            mav.viewName = "error"
            return mav
        }
        return completeTransition(mav, rsvId, ActionType.CHANGE, request)
    }

    //取消完了画面
    @PostMapping("app/cancelComplete")
    fun cancelComplete(mav: ModelAndView,
                       @RequestParam("id") id: String,
                       request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        //取消処理
        val rsvId: String
        try {
            rsvId = rsvChangeService.delete(id, request)
        } catch (e: Exception) {
            mav.viewName = "error"
            return mav
        }
        return completeTransition(mav, rsvId, ActionType.CHANGE, request)
    }

    fun completeTransition(mav: ModelAndView, id: String, actionType: Enum<ActionType>,
                           request: HttpServletRequest): ModelAndView {
        val rsvDetail = rsvDetailService.getDetailForActionComplete(id)

        mav.viewName = "complete"
        mav.addObject("rsvDetail", rsvDetail)

        when (actionType) {
            ActionType.ADD -> {
                mav.addObject("title", "予約完了")
                mav.addObject("headText",
                        "タクシーのご予約内容がタクシー会社に送信されました。")
                mav.addObject("message",
                        "タクシー会社より予約の受付が完了したら、スマホに通知され、代表者にメールが送付されます。\n" +
                                "ステータスは予約確定済となります。\n" +
                                "\n" +
                                "注意：予約完了ではありません。")
            }

            ActionType.CHANGE -> {
                mav.addObject("title", "変更完了")
                mav.addObject("headText",
                        "ご予約の変更内容がタクシー会社に送信されました。")
                mav.addObject("message",
                        "タクシー会社より変更の受付が完了したら、スマホに通知され、代表者にメールが送付されます。")
            }

            ActionType.CANCEL -> {
                mav.addObject("title", "取消完了")
                mav.addObject("headText",
                        "タクシーのご予約を取り消しました。")
                mav.addObject("message",
                        "またのご利用をお待ちしております。")
            }
        }

        return mav
    }


}