package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*

/**
 * 登録フローの画面表示用コントローラクラス
 *   ・登録画面
 *   ・登録確認画面
 */
@Controller
class ReservationController(
        private val taxiRepository: TaxiInfoRepository
) {

    // TODO　全体　リポジトリーの結果を直接渡さない
    //登録画面
    @GetMapping("/app/registration")
    fun registration(mav: ModelAndView): ModelAndView {
        //TODO アプリ遷移かWeb遷移かを判定して表示項目の出し分け
        mav.viewName = "registration"
        mav.addObject("taxiList", taxiRepository.findAll())
        mav.addObject("reservationForm", ReservationForm())

        return mav
    }

    //登録確認画面
    @PostMapping("app/confirmation")
    fun confirmation(mav: ModelAndView, @Validated @ModelAttribute rsvForm: ReservationForm, result: BindingResult): ModelAndView {
        //TODO エラー時の画面表示
        //バリデートエラー
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiRepository.findAll())
            mav.addObject("errorMassage","未入力の項目があります。")
            return mav
        }
        //入力不正
        if (rsvFormValidate(rsvForm)) {
            println("メソッドの入力チェックでエラー")
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiRepository.findAll())
            return mav
        }

        println("チェックOK")

        //確認画面へ遷移
        mav.viewName = "confirmation"
        mav.addObject("reservationForm", rsvForm)

        //タクシー会社の表示用のオブジェクトを設置
        val taxiInformation = taxiRepository.findById(rsvForm.company_id).get()
        mav.addObject("taxiCompanyName", taxiInformation.company_name)
        return mav
    }
    // TODO バリデーションでクラスを切る
    fun rsvFormValidate(rsvForm: ReservationForm): Boolean {
        var isError = false

        //メール同一チェック
        if (rsvForm.mail != rsvForm.mailCheck) {
            println("メール不一致")
            isError = true
        }

        //タクシー会社リスト未選択エラー
        if (rsvForm.company_id == "") {
            println("タクシー会社未選択")
            isError = true
        }

        //乗車日付の過去日チェック
        val nowDate = Calendar.getInstance()
        nowDate.set(Calendar.HOUR_OF_DAY, 0)
        nowDate.set(Calendar.MINUTE, 0)
        nowDate.set(Calendar.SECOND, 0)
        nowDate.set(Calendar.MILLISECOND, 0)
        if (rsvForm.date.before(nowDate.time)) {
            println("乗車日が過去日")
            isError = true
        }

        return isError
    }

}