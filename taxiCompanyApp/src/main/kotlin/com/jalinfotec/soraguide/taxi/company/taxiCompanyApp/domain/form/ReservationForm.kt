package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import javax.validation.constraints.*

data class ReservationForm(
        var reservationId: String,
        var statusName: String,
        var rideOnDate: Date,
        var rideOnTime: Time,

        @get:NotNull
        @get:Max(value = 20)
        @get:Min(value = 0)
        var adult: Int,

        @get:NotNull
        @get:Max(20)
        @get:Min(value = 0)
        var child: Int,

        @get:NotNull
        @get:Max(20)
        @get:Min(value = 1)
        var carDispatchNumber: Int,

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var destination: String,

        @get:NotEmpty
        @get:Size(min = 1, max = 20)
        var passengerName: String,

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var passengerPhonetic: String,

        @get:NotEmpty
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]*")
        var passengerContact: String,

        @get:NotEmpty
        @Email
        @get:Size(min = 1, max = 50)
        var passengerMail: String,

        @get:NotNull
        @get:Size(max = 99)
        var comment: String,

        @get:NotNull
        @get:Size(max = 10)
        var carNumber: String,

        @get:NotNull
        @get:Size(max = 15)
        var carContact: String,

        @get:NotNull
        @get:Size(max = 99)
        var notice: String,
        var lastUpdate: Timestamp
)