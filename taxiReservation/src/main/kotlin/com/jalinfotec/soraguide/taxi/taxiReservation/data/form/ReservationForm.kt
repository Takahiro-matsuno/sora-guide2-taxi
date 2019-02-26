package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

import java.sql.Date
import java.sql.Time
import java.util.*
import javax.validation.constraints.*

data class ReservationForm(
        var id: String = "",

        var date: Date = Date(Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo")).timeInMillis),

        var time: String = Time(date.time).toString().substring(0, 5),

        @get:NotNull
        @get:Max(value = 20)
        var adult: Int = 1,

        @get:NotNull
        @get:Max(20)
        var child: Int = 0,

        @get:NotNull
        @get:Max(20)
        var car_dispatch: Int = 1,

        @get:NotEmpty
        var company_name: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var destination: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 20)
        var name: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var phonetic: String = "",

        @get:NotEmpty
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]*", message = "エラー")
        var phone: String = "",

        @get:NotEmpty
        @Email
        @get:Size(min = 1, max = 50)
        var mail: String = "",

        @Email
        var mailCheck: String = "",

        //任意項目
        @get:NotNull
        @get:Size(max = 99)
        var comment: String = ""
)