package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

import java.sql.Time
import java.sql.Date

class ReservationForm {
    var status: Int = 0
    var date: Date = Date(0)
    var time: String = Time(date.time).toString()
    var adult: Int = 0
    var child: Int = 0
    var taxi_number: Int = 0
    var company_id: String = ""
    var destination: String = ""
    var name: String = ""
    var phonetic: String = ""
    var phone: String = ""
    var mail: String = ""
    var mailCheck: String = ""
    var comment: String = ""
}