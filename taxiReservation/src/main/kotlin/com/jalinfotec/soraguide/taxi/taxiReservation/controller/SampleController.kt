package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.dao.TaxiInfoDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.persistence.EntityManager

//@Grab("thymeleaf-spring5")

@Controller
class SampleController {

    // 疎通確認
    @RequestMapping("hello")
    fun hello(@RequestParam(value = "name") name: String?): String {
        return if (name == null) "who are you?" else "name"
    }
    // データベースアクセス
    @RequestMapping("db")
    fun db() {

    }
    // thymeleaf表示
    @RequestMapping("view")
    fun view() {

    }
    // thymeleafにdbデータ埋め込み
    @RequestMapping("dbView")
    fun dbView() {

    }





    /*
    // URLに対応する処理を書いていく
    @RequestMapping(value = "/", method = [RequestMethod.GET])
    fun hello(): String/*MutableList<TaxiInformation>*/ {
        //タクシーの情報表示（テスト）

        val taxiInfo = TaxiInfoDao()
        val result = taxiInfo.getAll()

        //予約の情報表示（テスト）
        /*
        val bookingInfo = BookingInfoDao()
        val result = bookingInfo.getAll()
        */

        return "index"
    }*/

    /*
    お試しで作ったコントローラ
     */
    @Autowired
    lateinit var entityManager :EntityManager
    lateinit var taxiInfoDao : TaxiInfoDao

    @RequestMapping("/")
    @ResponseBody
    fun home(mav :ModelAndView): ModelAndView {
        mav.viewName = "index"
        return mav
    }

    @RequestMapping("/login")
    @ResponseBody
    fun login(mav :ModelAndView): ModelAndView {
        mav.viewName = "reservationDetail"
        mav.addObject("test","aaa")
        return mav
    }

    @RequestMapping("/dbTest")
    @ResponseBody
    fun dbRead(mav :ModelAndView):ModelAndView{
        mav.viewName = "dbRead"
        mav.addObject("dataList",taxiInfoDao.getAll())
        return mav
    }

    /*
    お試しここまで
     */

    /*
    // http://localhost:8080?param=hoge
    // みたいにGETパラメータをもらいたいときはこう
    @RequestMapping("/echo")
    fun paramTest(@RequestParam(value = "param") param: String?): String {
//        return "param is ${param ?: "nothing"}"
        val bookingInfo = BookingInfoDao()
        val result = bookingInfo.getAll()

        return result
    }*/
}