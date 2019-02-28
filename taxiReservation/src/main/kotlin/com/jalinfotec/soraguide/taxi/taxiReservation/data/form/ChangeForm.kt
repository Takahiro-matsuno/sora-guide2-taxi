package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import javax.validation.constraints.*

data class ChangeForm(
        var id: String = "",

        var date: Date = Date(0),

        var time: String = Time(date.time).toString().substring(0, 5),

        @get:NotNull(message = "大人")
        @get:Max(20)
        var adult: Int = 1,

        @get:NotNull(message = "子供")
        @get:Max(20)
        var child: Int = 0,

        @get:NotNull(message = "台数")
        @get:Max(20)
        var car_dispatch: Int = 1,

        @get:NotEmpty(message = "目的地")
        @get:Size(min = 1, max = 30)
        var destination: String = "",

        @get:NotEmpty(message = "電番")
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]*", message = "エラー")
        var phone: String = "",

        //任意項目
        @get:NotNull
        @get:Size(max = 99)
        var comment: String = "",

        var lastUpdate : Timestamp = Timestamp(0)
)