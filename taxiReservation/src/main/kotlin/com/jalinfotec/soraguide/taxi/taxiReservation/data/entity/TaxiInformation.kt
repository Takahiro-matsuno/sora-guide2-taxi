package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import javax.persistence.*

@Entity
@Table(name = "taxi_info")
data class TaxiInformation(
        @Id
        @Column(name="company_id")
        var id: String = "",

        @Column(name="company_name")
        var companyName: String = "",
        var contact: String = "",
        var location: String =""
)