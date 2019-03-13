package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.login

import org.springframework.context.ApplicationListener
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class LoginFailureEventListenter : ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    // アカウントロックをかける連続認証失敗回数の閾値
    private var loginAttemptsThreshold: Int = 0

    fun setLoginAttemptsThreshold(threshold: Int) {
        this.loginAttemptsThreshold = threshold
    }

    @EventListener
    override fun onApplicationEvent(event: AuthenticationFailureBadCredentialsEvent) {
        /*
        println("イベントリスナー${event.exception.javaClass}")

        if (event.exception.javaClass == UsernameNotFoundException::class.java) {
            println("存在しないアカウント")
            return
        }

        val userId = event.authentication.name

        recordLoginAttempts(userId)

        val failedLoginAttempts = countFailedLoginAttempts(userId)
        if (failedLoginAttempts == loginAttemptsThreshold) {
            lockoutUser(userId)
        }
        */
    }

    // ログイン失敗の情報を DB に記録...
    private fun recordLoginAttempts(userId: String) {
        println("DBに失敗情報書き込み")
    }

    // 何回連続でログインに失敗したかの情報を返す
    private fun countFailedLoginAttempts(userId: String): Int {
        println("連続失敗回数")
        return 0
    }

    // アカウントをロックアウトする
    private fun lockoutUser(userId: String) {
        println("ロックアウト")
    }
}