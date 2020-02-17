package com.jalinfotec.soraguide.taxi.taxiReservation.application.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().antMatchers("/**").permitAll()
        //TODO javascriptでformを送信しているページのcsrfチェックを暫定的に無効にしている。
        http.csrf()
                .ignoringAntMatchers("/app/detail","/app/change","/app/cancelComplete")
    }
}