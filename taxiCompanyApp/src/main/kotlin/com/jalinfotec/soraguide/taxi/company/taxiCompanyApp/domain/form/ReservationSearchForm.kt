package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

data class ReservationSearchForm(
        val reservationId: String = "",
        val reservationStatus: Array<String> = arrayOf(),
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