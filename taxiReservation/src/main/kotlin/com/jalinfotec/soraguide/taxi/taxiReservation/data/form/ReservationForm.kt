package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

import java.sql.Date
import java.sql.Time

class ReservationForm {
    var status: Int = 0
    var date: Date? = null
    var time: Time? = null
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