package com.jalinfotec.soraguide.taxi.taxiReservation.data.form

data class ListForm(
        var id: String = "",
        var reservationStatus: String = "",
        var rideOnDate: String = "",
        var rideOnTime: String = "",
        var destination: String = ""
)