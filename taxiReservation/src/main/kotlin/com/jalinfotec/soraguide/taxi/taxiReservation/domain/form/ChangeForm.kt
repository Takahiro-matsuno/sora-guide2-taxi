package com.jalinfotec.soraguide.taxi.taxiReservation.domain.form

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import javax.validation.constraints.*

data class ChangeForm(
        //変更不可
        var id: String = "",

        //変更不可
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

        //変更不可
        var companyName: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var destination: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 20)
        var passengerName: String = "",

//        @get:NotEmpty
//        @get:Size(min = 1, max = 30)
//        var passengerPhonetic: String = "",

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

        //変更不可
        var carNumber: String = "",
        var carContact: String = "",
        var notice: String = "",
        var lastUpdate : Timestamp = Timestamp(0)
)