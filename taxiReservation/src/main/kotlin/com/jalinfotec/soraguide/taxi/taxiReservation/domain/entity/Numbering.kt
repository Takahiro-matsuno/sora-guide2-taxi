package com.jalinfotec.soraguide.taxi.taxiReservation.domain.entity

import javax.persistence.*

@Entity
@Table(name = "numbering")
data class Numbering(
        @Id
        @Column(name = "tablename")
        var tableName: String = "",

        @Column(name = "nextvalue")
        var nextValue: Int = 1
)