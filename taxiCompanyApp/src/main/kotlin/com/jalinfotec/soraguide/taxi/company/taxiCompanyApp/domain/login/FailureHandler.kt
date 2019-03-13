package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.login

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FailureHandler(private val userAccountService: UserAccountService) : AuthenticationFailureHandler {

    companion object {
        // アカウントロックまでの連続ログイン失敗回数閾値
        const val ACCOUNT_LOCK = 30
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         ex: AuthenticationException) {
        // 処理後のリダイレクト先URL
        var redirectUrl = "/system-error"

        // エラー種別による処理の区別
        if (ex is BadCredentialsException) {
            //ユーザ名、またはパスワード誤りの場合
            redirectUrl = "/login-error"

            //入力されたユーザ名を取得
            val userName = request.getParameter("username")
            println(userName)

            //DBからユーザ情報を取得
            val account = userAccountService.findByUserName(userName)

            if (account != null) {
                //ユーザが存在する場合、失敗回数を加算
                println("失敗回数加算")
                account.failureCount++

                if (account.failureCount >= ACCOUNT_LOCK) {
                    //ログイン失敗回数が閾値を超えている場合、アカウントロック
                    println("アカウントロック")
                    account.lockFlag = true
                }

                //ユーザ情報の更新
                userAccountService.updateAccount(account)
            }
        } else if (ex is LockedException) {
            // アカウントロックされている場合
            redirectUrl = "/account-lock"
        }

        response.sendRedirect(redirectUrl)
    }


}