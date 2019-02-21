package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import org.springframework.stereotype.Controller
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
        private val rsvChangeService: ReservationChangeService
) {

    enum class ActionType { ADD, CHANGE, CANCEL }

    //登録完了画面
    @PostMapping("app/rsvComplete")
    fun rsvComplete(mav: ModelAndView,
                    @ModelAttribute("reservationForm") rsvForm: ReservationForm,
                    request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        var rsvId = ""
        //登録処理
        try {
            rsvId = rsvCompService.complete(rsvForm, request, response)
        } catch (e: Exception) {
            mav.viewName = "confirmation"
            mav.addObject("reservationForm", rsvForm)
        }

        return completeTransition(mav, rsvId, "reservation")
    }

    //変更完了画面
    @PostMapping("app/changeComplete")
    fun changeComplete(mav: ModelAndView,
                       @ModelAttribute("reservationForm") rsvForm: ReservationForm): ModelAndView {
        val rsvId: String

        //変更処理
        try {
            rsvId = rsvChangeService.change(rsvForm)
        } catch (e: Exception) {
            mav.viewName = "error"
            return mav
        }
        return completeTransition(mav, rsvId, "change")
    }

    //取消完了画面
    @PostMapping("app/cancelComplete")
    fun cancelComplete(mav: ModelAndView,
                       @RequestParam("id") id: String): ModelAndView {
        val rsvId: String

        //取消処理
        try {
            rsvId = rsvChangeService.delete(id)
        } catch (e: Exception) {
            mav.viewName = "error"
            return mav
        }
        return completeTransition(mav, rsvId, "cancel")
    }

    fun completeTransition(mav: ModelAndView, id: String, actionName: String): ModelAndView {
        val rsvDetail = rsvDetailService.getDetail(id)

        mav.viewName = "complete"
        mav.addObject("rsvDetail", rsvDetail.get())
        mav.addObject("statusText", rsvDetailService.statusText)

        when (actionName) {
            "reservation" -> {
                mav.addObject("title", "予約完了")
                mav.addObject("headText",
                        "タクシーのご予約内容がタクシー会社に送信されました。")
                mav.addObject("message",
                        "タクシー会社より予約の受付が完了したら、スマホに通知され、代表者にメールが送付されます。\n" +
                                "ステータスは予約確定済となります。\n" +
                                "\n" +
                                "注意：予約完了ではありません。")
            }

            "change" -> {
                mav.addObject("title", "変更完了")
                mav.addObject("headText",
                        "ご予約の変更内容がタクシー会社に送信されました。")
                mav.addObject("message",
                        "タクシー会社より変更の受付が完了したら、スマホに通知され、代表者にメールが送付されます。")
            }

            "cancel" -> {
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