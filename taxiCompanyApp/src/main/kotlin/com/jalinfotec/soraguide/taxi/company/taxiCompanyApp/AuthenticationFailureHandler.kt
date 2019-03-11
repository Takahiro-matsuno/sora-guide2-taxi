package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFailureHandler{

    @Throws(IOException::class, ServletException::class)
    fun onAuthenticationFailure(httpServletRequest: HttpServletRequest,
                                         httpServletResponse: HttpServletResponse,
                                         authenticationException: AuthenticationException) {
        var errorCode = ""

        // ExceptionからエラーIDをセットする
        if (authenticationException is BadCredentialsException) {
            errorCode = "403"
        }
        httpServletResponse.sendRedirect(httpServletRequest.contextPath + "/index?error=" + errorCode)
    }
}