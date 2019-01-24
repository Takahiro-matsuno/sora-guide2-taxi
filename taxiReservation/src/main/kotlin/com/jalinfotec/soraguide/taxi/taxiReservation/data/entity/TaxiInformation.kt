package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import javax.persistence.*

@Entity
@Table(name = "taxi_info")
data class TaxiInformation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var company_id: String = "",

        var name: String = "",
        var contact: String = "",
        var location: String =""
)