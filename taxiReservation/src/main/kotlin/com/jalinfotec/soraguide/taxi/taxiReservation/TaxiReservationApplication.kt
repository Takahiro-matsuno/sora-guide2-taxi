package com.jalinfotec.soraguide.taxi.taxiReservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class TaxiReservationApplication

fun main(args: Array<String>) {
    runApplication<TaxiReservationApplication>(*args)
}