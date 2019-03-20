package com.jalinfotec.soraguide.taxi.taxiReservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableRetry
@EnableScheduling
class TaxiReservationApplication
	fun main(args: Array<String>) {
		runApplication<TaxiReservationApplication>(*args)
	}