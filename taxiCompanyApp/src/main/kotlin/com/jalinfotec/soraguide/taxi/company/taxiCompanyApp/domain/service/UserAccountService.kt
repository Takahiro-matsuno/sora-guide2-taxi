package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.Account
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.UserSettingForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.AccountRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.TaxiCompanyRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class UserAccountService(
        private val taxiRepository: TaxiCompanyRepository,
        private val taxiCompanyService: TaxiCompanyService,
        private val accRepository: AccountRepository,
        private val passwordEncoder: PasswordEncoder,
        private val sendMailService: SendMailService
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class, LockedException::class)
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
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun findByCompanyIdAndUsername(companyId: String, username: String): Boolean {
        if (!taxiRepository.findById(companyId).isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return false
        }
        return true
    }

    // パスワード変更
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
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

    /**
     * ユーザ情報の取得
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun findByUserName(userName: String): Account? {
        return accRepository.findByUsername(userName)
    }

    /**
     * ユーザ情報の更新
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun updateAccount(account: Account) {
        accRepository.save(account)
    }

    /**
     * パスワードの初期化
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun resetPassword(userName: String, inputMail: String) {
        // ユーザ情報と会社情報の取得
        val account = accRepository.findByUsername(userName) ?: throw Exception()
        val companyInfo = taxiCompanyService.getCompanyInfo(account.companyId) ?: throw Exception()

        // メールアドレスの一致確認
        if (inputMail != companyInfo.companyMail) {
            throw Exception()
        }

        // 新パスワード生成
        val newPassword = createPassword()
        account.password = passwordEncoder.encode(newPassword)

        // ログイン失敗回数とアカウントロックのリセット
        account.failureCount = 0
        account.lockFlag = false

        // ユーザ情報の更新
        accRepository.save(account)

        //メール送信
        sendMailService.sendAccountResetMail(userName, newPassword, companyInfo.companyName, companyInfo.companyMail)
    }

    /**
     * ランダムなパスワード生成処理
     */
    private fun createPassword():String{
        //パスワード桁数
        val length = 8
        //アルファベット大文字小文字のスタイル(normal/lowerCase/upperCase)
        val style = "normal"

        //生成処理
        val result = StringBuilder()
        //パスワードに使用する文字を格納
        val source = StringBuilder()
        //数字
        for (i in 0x30..57) {
            source.append(i.toChar())
        }
        //アルファベット小文字
        when (style) {
            "lowerCase" -> {
            }
            else -> for (i in 0x41..90) {
                source.append(i.toChar())
            }
        }
        //アルファベット大文字
        when (style) {
            "upperCase" -> {
            }
            else -> for (i in 0x61..122) {
                source.append(i.toChar())
            }
        }

        val sourceLength = source.length
        val random = Random()
        while (result.length < length) {
            result.append(source[Math.abs(random.nextInt()) % sourceLength])
        }

        return result.toString()
    }
}