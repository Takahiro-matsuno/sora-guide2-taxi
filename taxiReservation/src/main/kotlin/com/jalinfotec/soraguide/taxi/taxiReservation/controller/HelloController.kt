package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.dao.BookingInfoDao
import com.jalinfotec.soraguide.taxi.taxiReservation.dao.TaxiInfoDao
import com.jalinfotec.soraguide.taxi.taxiReservation.item.TaxiInfoItem
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

//@Grab("thymeleaf-spring5")

@RestController
class HelloController {


    // URLに対応する処理を書いていく
    @RequestMapping(value = "/", method = [RequestMethod.GET])
    fun hello(): MutableList<TaxiInfoItem> {
        //タクシーの情報表示（テスト）

        val taxiInfo = TaxiInfoDao()
        val result = taxiInfo.readAll()

        //予約の情報表示（テスト）
        /*
        val bookingInfo = BookingInfoDao()
        val result = bookingInfo.readAll()
        */

        return result
    }

    // http://localhost:8080?param=hoge
    // みたいにGETパラメータをもらいたいときはこう
    @RequestMapping("/echo")
    fun paramTest(@RequestParam(value = "param") param: String?): String {
//        return "param is ${param ?: "nothing"}"
        val bookingInfo = BookingInfoDao()
        val result = bookingInfo.readAll()

        return result
    }
}