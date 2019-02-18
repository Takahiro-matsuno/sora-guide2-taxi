package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

import java.sql.Date
import java.sql.Time

class ReservationForm(
        var reservationId: String = "",
        var statusName: String = "",
        var rideOnDate: Date,
        var rideOnTime: Time,
        var adult: Int = 0,
        var child: Int = 0,
        var taxiNumber: Int = 0,
        var companyId: String = "",
        var destination: String = "",
        var passengerName: String = "",
        var passengerPhonetic: String = "",
        var passengerContact: String = "",
        var passengerMail: String = "",
        var comment: String = "",
        var carNumber: String = "",
        var carContact: String = "",
        var notice: String = ""
)