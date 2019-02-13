package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import javax.persistence.*

@Entity
@Table(name = "taxi_info")
data class TaxiInformation(
        @Id
        @Column(name="company_id")
        var id: String = "",

        var company_name: String = "",
        var contact: String = "",
        var location: String =""
)