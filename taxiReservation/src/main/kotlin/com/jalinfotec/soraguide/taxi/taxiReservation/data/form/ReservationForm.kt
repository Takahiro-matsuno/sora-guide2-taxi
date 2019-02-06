package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

import java.sql.Time
import java.sql.Date
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ReservationForm(
        //初期値:本日の日付
        @NotNull
        var date: Date = Date(Calendar.getInstance().timeInMillis),
        //??
        @NotEmpty
        var time: String = Time(date.time).toString().substring(0,5),

        @NotEmpty
        var adult: Int = 1,

        @NotEmpty
        var child: Int = 0,

        @NotEmpty
        var taxi_number: Int = 1,

        @NotNull
        var company_id: String = "",

        @NotNull
        var destination: String = "",

        @NotNull
        var name: String = "",

        @NotNull
        var phonetic: String = "",

        @NotNull
        var phone: String = "",

        @NotEmpty
        @Email
        var mail: String = "",

        @NotEmpty
        @Email
        var mailCheck: String = "",

        //任意項目
        var comment: String = ""
)