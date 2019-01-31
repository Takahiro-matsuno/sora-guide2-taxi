package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

/**
 * サンプル用controllerクラス
 * おためし用のクラスのため、後々削除する。
 */
//TODO クラス削除
@Controller
class SampleController(
        private val taxiRepository: TaxiInfoRepository,
        private val bookingRepository: BookingInfoRepository,
        private val rdb : ReservationDetailService,
        private val rsvCompService: ReservationCompleteService
) {

    // 疎通確認
    @RequestMapping("hello")
    fun hello(@RequestParam(value = "name") name: String?): String {
        return if (name == null) "who are you?" else "name"
    }

    // データベースアクセス
    @RequestMapping("db")
    fun db(mav: ModelAndView, @RequestParam("id") id: String?): ModelAndView {
        mav.viewName = "dbRead"
        if (id != null) {
            mav.addObject("dataList", taxiRepository.findById(id))
        } else {
            mav.addObject("dataList", taxiRepository.findAll())
        }
        return mav
    }

    // thymeleaf表示
    @RequestMapping("view")
    fun view() {

    }

    // thymeleafにdbデータ埋め込み
    @RequestMapping("dbView")
    fun dbView() {

    }

    @RequestMapping("/")
    @ResponseBody
    fun home(mav: ModelAndView): ModelAndView {
        mav.viewName = "index"
        return mav
    }

    @RequestMapping("/login")
    @ResponseBody
    fun login(mav: ModelAndView): ModelAndView {
        mav.viewName = "reservationDetail"
        mav.addObject("test", "aaa")
        return mav
    }

    @RequestMapping("/bookingInfo")
    @ResponseBody
    fun detailTest(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        mav.viewName = "reservationDetail"
        println(id)
        mav.addObject("dataList", rdb.getDetail(id))
        mav.addObject("status", rdb.statusText)
        mav.addObject("companyName", rdb.taxiCompanyName)
        return mav
    }

    @RequestMapping("/changeTest")
    @ResponseBody
    fun changeTest(mav: ModelAndView): ModelAndView {
        mav.viewName = "index"
        val bookInfo = bookingRepository.findById("0000000001").get()
        bookInfo.name = "江戸川コナン"
        bookInfo.phonetic = "エドガワコナン"
        bookingRepository.save(bookInfo)
        return mav
    }

    /**
     * 登録処理テスト用
     */
    @RequestMapping("/reservationTest")
    @ResponseBody
    fun reservationTest(mav: ModelAndView): ModelAndView {
        mav.viewName = "registrationTest"
        mav.addObject("taxiList", taxiRepository.findAll())
        mav.addObject("reservationForm", ReservationForm())

        return mav
    }

    @RequestMapping("/reservationCompTest")
    @ResponseBody
    fun reservationCompTest(mav: ModelAndView,
                            @ModelAttribute("reservationForm") rsvForm: ReservationForm): ModelAndView {
        //登録処理
        rsvCompService.setBooking(rsvForm)
        rsvCompService.complete()

        //完了画面の確認は別途
        mav.viewName = "login"
        return mav
    }
}