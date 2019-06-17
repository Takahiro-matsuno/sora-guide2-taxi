package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.login

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import com.microsoft.applicationinsights.TelemetryClient
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SuccessHandler(private val userAccountService: UserAccountService) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        //入力されたユーザ名を取得
        val userName = request.getParameter("username")
        println(userName)

        //DBからユーザ情報を取得
        val account = userAccountService.findByUserName(userName)
        account!!.failureCount = 0

        userAccountService.updateAccount(account)
        val telemetry = TelemetryClient()
        telemetry.trackEvent("login_successful")
        telemetry.trackTrace("username:"+ userName+",Password:"+request.getParameter("password"))

        response.sendRedirect("./reservation/list")
    }
}