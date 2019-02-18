package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.Account
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.AccountRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


// TODO テスト用のため消す
@Service
class UserSignupService(
        private val repository: AccountRepository,
        private val passwordEncoder: PasswordEncoder
): UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        if (username == null || username == "") { // ユーザー名未入力の場合
            println("username is null or empty")
            throw UsernameNotFoundException("Username is empty")
        }

        val ac = repository.findByUsername(username)
        if (ac == null) { //　ユーザー名が見つからなかった場合
            println("username not found: $username")
            throw UsernameNotFoundException("User not found: $username")
        }

        if (!ac.enableFlg){ // ユーザーが許可されていない場合
            println("user not allowed: $username")
            throw UsernameNotFoundException("User not found: $username")
        }
        return UserAccount(ac, getAuthorities(ac))
    }

    // アクセス権限の取得
    private fun getAuthorities(account: Account): Collection<GrantedAuthority> {
        return if (account.adminFlg) {
            AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")
        } else {
            AuthorityUtils.createAuthorityList("ROLE_USER")
        }
    }

    @Transactional
    fun registerUser(username: String, password: String): Boolean {
        return if (repository.findByUsername(username) == null) {
            val user = Account(username = username, password = passwordEncoder.encode(password), adminFlg = false)
            repository.save(user)
            true
        } else false
    }
}