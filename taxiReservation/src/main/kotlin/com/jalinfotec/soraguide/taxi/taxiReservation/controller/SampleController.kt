package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

/**
 * サンプル用controllerクラス
 * おためし用のクラスのため、後々削除する。
 */
//TODO クラス削除
@Controller
class SampleController {

    @Autowired
    var taxiRepository: TaxiInfoRepository? = null
    @Autowired
    var bookingRepository: BookingInfoRepository? = null
    @Autowired
    val rdb = ReservationDetailService()

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
            mav.addObject("dataList", taxiRepository?.findById(id))
        } else {
            mav.addObject("dataList", taxiRepository?.findAll())
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
        val bookInfo = bookingRepository?.findById("0000000001")?.get()!!
        bookInfo.name = "江戸川コナン"
        bookInfo.phonetic="エドガワコナン"
        bookingRepository?.save(bookInfo)
        return mav
    }
}