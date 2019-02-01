package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import org.hibernate.Hibernate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import java.util.Optional.*

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
        private val rsvCompService: ReservationCompleteService,
        private val rsvDetailservice:ReservationDetailService,
        private val rsvChangeService:ReservationChangeService
) {

    // 疎通確認
    @RequestMapping("hello")
    fun hello(@RequestParam(value = "name") name: String?): String {
        return if (name == null) "who are you?" else "name"
    }

    // データベースアクセス
    @RequestMapping("db")
    fun db(mav: ModelAndView, @RequestParam("id") id: String?): ModelAndView {
        mav.viewName = "/testHTML/dbRead"
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
        mav.viewName = "testHTML/reservationList"
        return mav
    }

    @RequestMapping("/login")
    @ResponseBody
    fun login(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/reservationDetail"
        mav.addObject("test", "aaa")
        return mav
    }

    @RequestMapping("/bookingInfo")
    @ResponseBody
    fun detailTest(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        mav.viewName = "testHTML/reservationDetail"
        mav.addObject("dataList", rdb.getDetail(id))
        mav.addObject("status", rdb.statusText)
        mav.addObject("companyName", rdb.taxiCompanyName)
        return mav
    }

    @RequestMapping("/changeTest")
    @ResponseBody
    fun changeTest(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/index"
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
        mav.viewName = "testHTML/registrationTest"
        mav.addObject("taxiList", taxiRepository.findAll())
        mav.addObject("reservationForm", rsvDetailservice.getChangeDetail("0000000003"))

        return mav
    }

    @RequestMapping("/reservationCompTest")
    @ResponseBody
    fun reservationCompTest(mav: ModelAndView,
                            @ModelAttribute("reservationForm") rsvForm: ReservationForm,
                            @ModelAttribute("id")id:String,
                            @ModelAttribute("name")name:String): ModelAndView {

        val checkBooking = bookingRepository.findById(id)
        //検索にかからない場合
        if(!checkBooking.isPresent){
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }
        //ID改ざん対策
        if(checkBooking.get().name.trim() != name.trim()){
            println("ERROR：変更前後の予約で名前が一致しない")
            println("DB->${checkBooking.get().name.trim()}")
            println("入力->${name.trim()}")
            throw Exception()
        }

        //変更処理
        rsvChangeService.change(id,rsvForm)

        //完了画面の確認は別途
        mav.viewName = "testHTML/login"
        return mav
    }
}