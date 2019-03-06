package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

import java.sql.Date
import java.sql.Time
import java.util.*
import javax.validation.constraints.*

data class ReservationForm(
        var id: String = "",

        var rideOnDate: Date = Date(Calendar.getInstance().timeInMillis),

        var rideOnTime: String = Time(rideOnDate.time).toString().substring(0, 5),

        @get:NotNull
        @get:Max(value = 20)
        @get:Min(value = 0)
        var adult: Int = 1,

        @get:NotNull
        @get:Max(20)
        @get:Min(value = 0)
        var child: Int = 0,

        @get:NotNull
        @get:Max(20)
        @get:Min(value = 1)
        var carDispatchNumber: Int = 1,

        @get:NotEmpty
        var companyName: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var destination: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 20)
        var passengerName: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var passengerPhonetic: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]*")
        var passengerContact: String = "",

        @get:NotEmpty
        @Email
        @get:Size(min = 1, max = 50)
        var mail: String = "",

        @Email
        var mailCheck: String = "",

        //任意項目
        @get:NotNull
        @get:Size(max = 99)
        var comment: String = "",

        var rideOnDateStr: String = ""
)