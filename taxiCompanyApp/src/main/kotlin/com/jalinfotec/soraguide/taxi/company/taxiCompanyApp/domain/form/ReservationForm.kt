package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

import java.sql.Date
import java.sql.Time

data class ReservationForm(
        var reservationId: String,
        var statusName: String,
        var rideOnDate: Date,
        var rideOnTime: Time,
        var adult: Int,
        var child: Int,
        var carDispatchNumber: Int,
        var destination: String,
        var passengerName: String,
        var passengerPhonetic: String,
        var passengerContact: String,
        var passengerMail: String,
        var comment: String,
        var carNumber: String,
        var carContact: String,
        var notice: String
)