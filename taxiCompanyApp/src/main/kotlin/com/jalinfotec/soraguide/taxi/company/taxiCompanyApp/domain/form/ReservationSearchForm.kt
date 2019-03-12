package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants

data class ReservationSearchForm(
        val reservationId: String = "",
        val reservationStatus: Array<String> = Constants.reservationStatus.values.toTypedArray(),
        var passengerContact: String = "",
        var passengerName: String = "",
        var passengerPhonetic: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReservationSearchForm

        if (!reservationStatus.contentEquals(other.reservationStatus)) return false

        return true
    }

    override fun hashCode(): Int {
        return reservationStatus.contentHashCode()
    }
}