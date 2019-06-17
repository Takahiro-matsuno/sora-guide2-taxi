package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class TaxiCompanyForm(
        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        val companyName: String,

        @get:NotEmpty
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]*")
        val companyContact:String
)