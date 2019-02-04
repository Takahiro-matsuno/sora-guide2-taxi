package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.reservation

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ReservationRestController {

    //
    @RequestMapping(value = ["/reservation/update"], method = [RequestMethod.GET])
    fun getUpdate(): String {
        return ""
    }
}