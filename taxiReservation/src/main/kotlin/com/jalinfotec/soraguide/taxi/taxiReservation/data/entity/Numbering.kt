package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import javax.persistence.*

@Entity
@Table(name = "numbering")
data class Numbering(
        @Id
        @Column(name = "tablename")
        var name: String = "",

        @Column(name = "nextvalue")
        var nextValue: Int = 1
)