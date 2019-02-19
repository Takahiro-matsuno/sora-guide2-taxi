package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form

data class UserSettingForm(
        val username: String,
        val nowPassword: String = "",
        val newPassword: String = ""
)