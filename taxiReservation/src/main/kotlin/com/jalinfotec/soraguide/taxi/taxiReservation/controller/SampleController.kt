package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
class SampleController {

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

    @Autowired
    var taxiRepository: TaxiInfoRepository? = null

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
}