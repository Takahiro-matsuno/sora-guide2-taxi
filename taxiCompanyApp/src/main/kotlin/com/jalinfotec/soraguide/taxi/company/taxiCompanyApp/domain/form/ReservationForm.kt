package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

import java.sql.Date
import java.sql.Time

class ReservationForm(
        var id: String = "",
        var status: Int = 0,
        var date: Date? = null,
        var time: Time? = null,
        var adult: Int = 0,
        var child: Int = 0,
        var taxi_number: Int = 0,
        var companyId: String = "",
        var destination: String = "",
        var name: String = "",
        var phonetic: String = "",
        var phone: String = "",
        var mail: String = "",
        var comment: String = "",
        var car_number: String = "",
        var car_contact: String = "",
        var notice: String = ""
)