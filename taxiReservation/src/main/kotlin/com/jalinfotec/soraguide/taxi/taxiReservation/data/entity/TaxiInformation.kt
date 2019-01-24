package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import javax.persistence.*

@Entity
@Table(name = "taxi_info")
data class TaxiInformation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="company_id")
        var id: String = "",

        var name: String = "",
        var contact: String = "",
        var location: String =""
)