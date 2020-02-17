package com.jalinfotec.soraguide.taxi.taxiReservation.application.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.utils.cookie.UserAgentManager
import com.jalinfotec.soraguide.taxi.taxiReservation.application.form.ChangeForm
import com.jalinfotec.soraguide.taxi.taxiReservation.application.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.ReservationCompleteService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.service.TaxiInformationService
import com.jalinfotec.soraguide.taxi.taxiReservation.domain.validation.FormValidate
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 各種完了画面の表示用コントローラクラス
 *   ・登録完了
 *   ・変更完了
 *   ・取消完了
 */
@Controller
class ActionCompleteController(
        private val rsvCompService: ReservationCompleteService,
        private val rsvDetailService: ReservationDetailService,
        private val rsvChangeService: ReservationChangeService,
        private val taxiInformationService: TaxiInformationService
) {

    enum class ActionType { ADD, CHANGE, CANCEL }

    /**
     * 登録完了処理
     */
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
            mav.addObject("errorMessage", messageList[0].defaultMessage)
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
        var rsvId: String
        try {
            rsvId = rsvCompService.complete(rsvForm, request)
        } catch (e: Exception) {
            mav.viewName = "confirmation"
            mav.addObject("reservationForm", rsvForm)
            return mav
        }

        //予約処理完了後クッションページに遷移
        mav.viewName = "temp"
        mav.addObject("id", rsvId)

        return mav
    }

    /**
     * 変更完了画面
     */
    @PostMapping("app/changeComplete")
    fun changeComplete(mav: ModelAndView,
                       @Validated @ModelAttribute("changeForm") rsvForm: ChangeForm,
                       result: BindingResult, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        //単項目チェック
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            val messageList = result.fieldErrors
            mav.viewName = "change"
            mav.addObject("errorMessage", messageList[0].defaultMessage)
            return mav
        }

        //相関チェックと不足している単項目チェック
        val formValidateMessage = FormValidate().changeCheck(rsvForm)

        if (formValidateMessage.isNotEmpty()) {
            println("メソッドの入力チェックでエラー")
            mav.viewName = "change"
            mav.addObject("errorMessage", formValidateMessage)
            return mav
        }

        //変更処理
        val rsvId = rsvChangeService.change(rsvForm, request) ?: throw Exception()

        return completeTransition(mav, rsvId, ActionType.CHANGE, request)
    }

    /**
     * 取消完了画面
     */
    @PostMapping("app/cancelComplete")
    fun cancelComplete(mav: ModelAndView,
                       @RequestParam("id") id: String,
                       @RequestParam("lastUpdate") lastUpdate: Timestamp,
                       request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        //取消処理
        val rsvId = rsvChangeService.delete(id, lastUpdate, request) ?: throw Exception()

        return completeTransition(mav, rsvId, ActionType.CANCEL, request)
    }

    /**
     *予約完了画面表示
     */
    @PostMapping("app/complete")
    fun displayComplete(mav: ModelAndView,
                        @RequestParam("id") id: String,
                        request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        return completeTransition(mav, id, ActionType.ADD, request)
    }

    /**
     * 完了画面表示用の項目設定処理
     */
    fun completeTransition(mav: ModelAndView, id: String, actionType: Enum<ActionType>,
                           request: HttpServletRequest): ModelAndView {
        val rsvDetail = rsvDetailService.getDetailForActionComplete(id)

        mav.viewName = "complete"
        mav.addObject("rsvDetail", rsvDetail)

        //スマホアプリ遷移の場合は「予約一覧へ」ボタン表示
        var isListButton = false
        if (UserAgentManager().checkAndroidApp(request)) {
            isListButton = true
        }
        mav.addObject("isListButton", isListButton)

        when (actionType) {
            ActionType.ADD -> {
                mav.addObject("title", "予約完了")
                mav.addObject("headText",
                        "タクシーのご予約内容がタクシー会社に送信されました。")
                mav.addObject("message",
                        "タクシー会社より予約の受付が完了したら、代表者にメールが送付されます。\n" +
                                "予約の変更、または取消を希望されるお客様は予約詳細から操作いただくか、ご利用のタクシー会社へ直接ご連絡ください。")
            }

            ActionType.CHANGE -> {
                mav.addObject("title", "変更完了")
                mav.addObject("headText",
                        "ご予約の変更内容がタクシー会社に送信されました。")
                mav.addObject("message",
                        "タクシー会社より変更の受付が完了したら、代表者にメールが送付されます。\n" +
                                "追加で予約の変更、または取消を希望されるお客様は再度予約詳細から操作していただくか、ご利用のタクシー会社へ直接ご連絡ください。")
            }

            ActionType.CANCEL -> {
                mav.addObject("title", "取消完了")
                mav.addObject("headText",
                        "タクシーのご予約を取消しました。")
                mav.addObject("message",
                        "またのご利用をお待ちしております。")
            }
        }

        return mav
    }


}