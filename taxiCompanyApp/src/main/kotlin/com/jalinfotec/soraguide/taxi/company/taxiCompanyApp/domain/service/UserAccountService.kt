package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.Account
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.UserSettingForm
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.AccountRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.TaxiCompanyRepository
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAccountService(
        private val taxiRepository: TaxiCompanyRepository,
        private val accRepository: AccountRepository,
        private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {

        if (username == null || username == "") { // ユーザー名未入力の場合
            println("username is null or empty")
            throw UsernameNotFoundException("Username is empty")
        }

        val ac = accRepository.findByUsername(username)
        if (ac == null) { //　ユーザー名が見つからなかった場合
            println("username not found: $username")
            throw UsernameNotFoundException("User not found: $username")
        }

        if (!ac.enableFlg) { // ユーザーが許可されていない場合
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
        accRepository!!.save(user)
    }
    */
    // ユーザー取得
    @Transactional
    fun findByCompanyIdAndUsername(companyId: String, username: String): Boolean {
        if (!taxiRepository.findById(companyId).isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return false
        }
        return true
    }

    // パスワード変更
    @Transactional
    fun changePassword(account: UserAccount, usForm: UserSettingForm): Boolean {

        val companyId = account.getCompanyId()

        if (!taxiRepository.findById(companyId).isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return false
        }

        // ユーザー取得、見つからない場合は処理終了
        val user = accRepository.findByCompanyIdAndUsername(companyId, account.username) ?: return false

        //現パスワード照会
        //return if (passwordEncoder.encode(usForm.nowPassword) == user.password) {
        return if (passwordEncoder.matches(usForm.nowPassword, user.password)) {
            // 現在パスワードが一致する場合はパスワードを更新
            user.password = passwordEncoder.encode(usForm.newPassword)
            accRepository.save(user)
            true
        } else {
            println(passwordEncoder.encode(usForm.nowPassword))
            println(user.password)
            false
        }
    }
}