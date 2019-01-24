package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import java.sql.Date
import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "booking_info")
data class BookingInformation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "booking_id")
        var id: String,

        var status: Int,
        var date: Date,
        var time: Time,
        var adult: Int,
        var child: Int,
        var taxi_number: Int,
        var company_id: String,
        var destination: String,
        var name: String,
        var phonetic: String,
        var phone: String,
        var mail: String,
        var comment: String,
        var car_number: String,
        var car_contact: String,
        var notice: String
        )