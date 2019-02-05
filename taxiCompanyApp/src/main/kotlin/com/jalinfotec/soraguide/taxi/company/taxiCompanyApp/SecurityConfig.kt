package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.WebSecurity

/**
 * ログイン画面からの[/user]へのpostを拾い認証を行う
 */
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var uas: UserAccountService

    // todo ２つの違い
    override fun configure(web: WebSecurity?) {
        // セキュリティ設定を無視するリクエスト設定
        // 静的リソース(images、css、javascript)に対するアクセスはセキュリティ設定を無視する
        web?.ignoring()?.antMatchers(
                "/images/**",
                "/css/**",
                "/javascript/**",
                "/webjars/**")
    }
    //
    override fun configure(http: HttpSecurity?) {

        if (http != null) {

            //
            http.authorizeRequests()
                    .antMatchers(
                            "/",
                            "/user",
                            "/user-error",
                            "/signup",
                            "signup-error"
                    ).permitAll() // indexは全ユーザーアクセス許可
                    .anyRequest().authenticated()  // それ以外は全て認証無しの場合アクセス不許可

            // ログイン設定
            http.formLogin()
                    // ログインページのURL
                    .loginPage("/user")
                    // ログイン処理URL　[/user]にPOSTされる
                    .loginProcessingUrl("/user")
                    // 認証パラメータ
                    .usernameParameter("username")
                    .passwordParameter("password")
                    // ログイン成功時のURL
                    .defaultSuccessUrl("/reservation/list")
                    // ログイン失敗時のURL
                    .failureUrl("/user-error")
                    // ログインページへのアクセス許可
                    .permitAll()

            // ログアウト設定
            http.logout()
                    // ログアウト処理URL　[/logout]にPOSTされる
                    .logoutUrl("/logout")
                    // ログアウト時URL
                    .logoutSuccessUrl("/user-logout")
                    // ログアウトページへのアクセスを許可
                    .permitAll()
        }
    }

    @Autowired
    @Throws(Exception::class)
    fun configureAuthenticationManager(auth: AuthenticationManagerBuilder) {
        // 認証するユーザーを設定する
        auth.userDetailsService(uas)
                // 入力値をbcryptでハッシュ化した値でパスワード認証を行う
                .passwordEncoder(passwordEncoder())
    }
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
/*
    @Configuration
    protected class AuthenticationConfiguration : GlobalAuthenticationConfigurerAdapter() {
        @Autowired
        lateinit var uas: UserAccountService

        @Throws(Exception::class)
        override fun init(auth: AuthenticationManagerBuilder?) {
            // 認証するユーザーを設定する
            auth!!.userDetailsService<UserAccountService>(uas)
                    // 入力値をbcryptでハッシュ化した値でパスワード認証を行う
                    .passwordEncoder(BCryptPasswordEncoder())

        }
    }
    */
}