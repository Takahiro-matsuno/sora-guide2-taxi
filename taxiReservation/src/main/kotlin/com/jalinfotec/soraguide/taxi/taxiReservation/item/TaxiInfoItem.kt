package com.jalinfotec.soraguide.taxi.taxiReservation.item

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "taxi_info")
data class TaxiInfoItem(
        @Id
        val company_id: String,
        val name: String,
        val contact: String,
        val location: String
)