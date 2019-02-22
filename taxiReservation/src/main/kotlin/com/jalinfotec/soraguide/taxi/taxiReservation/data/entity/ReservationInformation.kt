package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "reservation_info")
data class ReservationInformation(
        @Id
        @Column(name = "reservation_id")
        var id: String = "",

        var status: Int = 0,

        @Column(name = "ride_on_date")
        var date: Date = Date(0),

        @Column(name = "ride_on_time")
        var time: Time = Time(date.time),

        var adult: Int = 0,
        var child: Int = 0,
        var car_dispatch_number: Int = 0,
        var company_id: String = "",
        var destination: String = "",
        var passenger_name: String = "",
        var passenger_phonetic: String = "",

        @Column(name = "passenger_contact")
        var phone: String = "",

        var mail: String = "",
        var comment: String = "",
        var car_number: String = "",
        var car_contact: String = "",
        var notice: String = "",
        var uuid: String = "",
        var last_update: Timestamp = Timestamp(0)
)