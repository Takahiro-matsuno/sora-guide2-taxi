package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.dao.TaxiInfoDao
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

    // URLに対応する処理を書いていく
    @RequestMapping(value = "/", method = [RequestMethod.GET])
    fun hello(): String {
        var taxiInfo = TaxiInfoDao()
        taxiInfo.read()
        return "Hello World"
    }

    // http://localhost:8080?param=hoge
    // みたいにGETパラメータをもらいたいときはこう
    @RequestMapping("/echo")
    fun paramTest(@RequestParam(value = "param") param: String?): String {
        return "param is ${param ?: "nothing"}"
    }
}