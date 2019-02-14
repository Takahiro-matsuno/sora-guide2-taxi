package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.Account
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.AccountRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAccountService : UserDetailsService {

    @Autowired
    private lateinit var repository: AccountRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

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
    /*
    // 管理者追加
    @Transactional
    fun registerAdmin(username: String, password: String) {
        val user = Account(username = username, password = passwordEncoder!!.encode(password), isAdmin = true)
        repository!!.save(user)
    }
    */
    @Transactional
    fun registerUser(username: String, password: String): Boolean {
        return if (repository.findByUsername(username) == null) {
            val user = Account(username = username, password = passwordEncoder.encode(password), adminFlg = false)
            repository.save(user)
            true
        } else false
    }
    @Transactional
    fun findByUsername(username: String): Account? {
        return repository.findByUsername(username)
    }
    @Transactional
    fun changePassword(username: String, nowPassword: String, newPassword: String): Boolean {
        val user = repository.findByUsername(username) ?: return false // エラー

        return if (passwordEncoder.encode(nowPassword) != user.password) {
            // TODO パスワードが違います。
            false
        } else {
            user.password = passwordEncoder.encode(newPassword)
            repository.save(user)
            true
        }
    }
}