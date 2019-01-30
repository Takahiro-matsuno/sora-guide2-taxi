package com.jalinfotec.soraguide.taxi.taxiReservation.data.entity

import javax.persistence.*

@Entity
@Table(name = "numbering")
data class Numbering (
    @Id
    var tableName:String="",
    var nextValue:Int
)