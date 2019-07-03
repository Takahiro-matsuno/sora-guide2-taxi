package com.jalinfotec.soraguide.taxi.taxiReservation.domain.form

import java.sql.Timestamp

data class DetailForm(
        var id: String = "",
        var reservationStatus: String = "",
        var rideOnDate: String = "",
        var rideOnTime: String = "",
        var adult: String = "",
        var child: String = "",
        var carDispatchNumber: String = "",
        var companyName: String = "",
        var destination: String = "",
        var passengerName: String = "",
        var passengerPhonetic: String = "",
        var passengerContact: String = "",
        var mail: String = "",
        var comment: String = "",
        var carNumber: String = "",
        var carContact: String = "",
        var notice: String = "",
        var lastUpdate: Timestamp = Timestamp(0),
        var isChange: Boolean = false,
        var companyContact: String = ""
)