package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFailureHandler: AuthenticationFailureHandler {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(httpServletRequest: HttpServletRequest,
                                         httpServletResponse: HttpServletResponse,
                                         authenticationException: AuthenticationException) {
        println(authenticationException)

        // ExceptionからエラーIDをセットする
        if (authenticationException is BadCredentialsException) {
            val userName = httpServletRequest.getParameter("username")
            println(userName)
        }

        httpServletResponse.sendRedirect(httpServletRequest.contextPath + "/index-error")
    }
}