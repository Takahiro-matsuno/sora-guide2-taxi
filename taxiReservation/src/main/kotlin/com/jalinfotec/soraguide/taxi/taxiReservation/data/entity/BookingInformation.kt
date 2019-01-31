package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import java.sql.Date
import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "booking_info")
data class BookingInformation(
        @Id
        @Column(name = "booking_id")
        var id: String = "初期値",

        var status: Int = 0,
        var date: Date = Date(0),
        var time: Time = Time(date.time),
        var adult: Int = 0,
        var child: Int = 0,
        var taxi_number: Int = 0,
        var company_id: String = "",
        var destination: String = "",
        var name: String = "",
        var phonetic: String = "",
        var phone: String = "",
        var mail: String = "",
        var comment: String = "",
        var car_number: String = "",
        var car_contact: String = "",
        var notice: String = ""
)