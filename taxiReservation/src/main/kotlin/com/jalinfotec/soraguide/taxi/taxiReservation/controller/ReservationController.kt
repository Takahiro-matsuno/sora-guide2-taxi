package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.TaxiInformationService
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
        private val taxiInfoService: TaxiInformationService
) {

    // TODO　全体　リポジトリーの結果を直接渡さない
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

        mav.addObject("taxiList", taxiInfoService.getTaxiNameList())
        mav.addObject("reservationForm", setRsvForm(ReservationForm()))
        mav.addObject("isTab", isTabDisplay)

        return mav
    }

    //登録確認画面
    @PostMapping("app/confirmation")
    fun confirmation(mav: ModelAndView, @Validated @ModelAttribute rsvForm: ReservationForm, result: BindingResult): ModelAndView {
        //TODO エラー時の画面表示
        //単項目チェック
        if (result.hasErrors()) {
            println("フォームの入力チェックでエラー")
            val messageList = result.fieldErrors
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiInfoService.getTaxiNameList())
            mav.addObject("errorMassage", messageList[0].defaultMessage)
            return mav
        }

        //相関チェックと不足している単項目チェック
        if (rsvFormValidate(rsvForm)) {
            println("メソッドの入力チェックでエラー")
            mav.viewName = "registration"
            mav.addObject("taxiList", taxiInfoService.getTaxiNameList())
            return mav
        }

        //確認画面へ遷移
        mav.viewName = "confirmation"
        mav.addObject("reservationForm", rsvForm)

        //タクシー会社の表示用のオブジェクトを設置
        //val taxiInformation = taxiRepository.findById(rsvForm.company_name).get()
        //mav.addObject("taxiCompanyName", taxiInformation.company_name)
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
        if (rsvForm.company_name == "") {
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