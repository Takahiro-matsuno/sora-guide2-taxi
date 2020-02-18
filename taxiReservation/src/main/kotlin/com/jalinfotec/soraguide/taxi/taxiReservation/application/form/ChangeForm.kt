package com.jalinfotec.soraguide.taxi.taxiReservation.application.form

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import javax.validation.constraints.*

data class ChangeForm(
        var id: String = "",
        var reservationStatus: String = "",
        var rideOnDate: Date = Date(0),
        var rideOnTime: String = Time(rideOnDate.time).toString().substring(0, 5),
        @get:NotNull
        @get:Max(20)
        var adult: Int = 1,
        @get:NotNull
        @get:Max(20)
        var child: Int = 0,
        @get:NotNull
        @get:Max(20)
        var carDispatchNumber: Int = 1,
        var companyName: String = "",
        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var destination: String = "",
        @get:NotEmpty
        @get:Size(min = 1, max = 20)
        var passengerName: String = "",
        @get:NotEmpty
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]{3}-[0-9]{4}-[0-9]{4}")
        var passengerContact: String = "",
        @get:NotEmpty
        @Email
        @get:Size(min = 1, max = 50)
        var mail: String = "",
        @Email
        var mailCheck: String = "",
        @get:NotNull
        @get:Size(max = 99)
        var comment: String = "",
        var carNumber: String = "",
        var carContact: String = "",
        var notice: String = "",
        var lastUpdate : Timestamp = Timestamp(0)
)