package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableRetry
@EnableScheduling
class TaxiCompanyAppApplication

fun main(args: Array<String>) {
	runApplication<TaxiCompanyAppApplication>(*args)
}