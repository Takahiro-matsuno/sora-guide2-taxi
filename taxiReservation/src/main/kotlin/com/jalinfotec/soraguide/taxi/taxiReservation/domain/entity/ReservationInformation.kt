package com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "reservation_info")
data class ReservationInformation(
        @Id
        @Column(name = "reservation_id")
        var reservationId: String = "",

        var status: Int = 0,

        @Column(name = "ride_on_date")
        var rideOnDate: Date = Date(0),

        @Column(name = "ride_on_time")
        var rideOnTime: Time = Time(rideOnDate.time),

        var adult: Int = 0,
        var child: Int = 0,

        @Column(name = "car_dispatch_number")
        var carDispatchNumber: Int = 0,

        @Column(name = "company_id")
        var companyId: String = "",
        var destination: String = "",

        @Column(name = "passenger_name")
        var passengerName: String = "",

//        @Column(name = "passenger_phonetic")
//        var passengerPhonetic: String = "",

        @Column(name = "passenger_contact")
        var passengerContact: String = "",

        var mail: String = "",
        var comment: String = "",

        @Column(name = "car_number")
        var carNumber: String = "",

        @Column(name = "car_contact")
        var carContact: String = "",
        var notice: String = "",
        var uuid: String = "",

        @Column(name = "last_update")
        var lastUpdate: Timestamp = Timestamp(0)
)