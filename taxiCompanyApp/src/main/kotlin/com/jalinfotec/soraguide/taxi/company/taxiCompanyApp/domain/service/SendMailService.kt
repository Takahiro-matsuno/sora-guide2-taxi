package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import com.sendgrid.*
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service

@Service
class SendMailService(
        private val sendGrid: SendGrid
) {

    /**
     * 旅客向けの案内メール送信処理
     */
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun sendMail(rsvInfo: ReservationInformation, taxiInfo: TaxiInformation, mailType: Enum<Constants.MAIL_TYPE>): Boolean {
        lateinit var response: Response
        try {
            // 送信するメールを作成する
            val mail = createMail(rsvInfo, taxiInfo, mailType)
            val request = Request()
            request.endpoint = "mail/send"
            request.method = Method.POST
            request.body = mail.build()
            // メール送信
            response = sendGrid.api(request)
        } catch (ex: Exception) {
            println(ex.message)
        }
        return response.statusCode in 200..299
    }

    /**
     * 旅客向けの案内メール作成処理
     */
    private fun createMail(rsvInfo: ReservationInformation, taxiInfo: TaxiInformation, mailType: Enum<Constants.MAIL_TYPE>): Mail {
        //メールに埋め込む動的項目のマップ化
        val replaceMap = mutableMapOf(
                "%name%" to rsvInfo.passengerName,
                "%name%" to rsvInfo.passengerName,
                "%rsvId%" to rsvInfo.reservationId,
                "%rideOnDate%" to rsvInfo.rideOnDate.toString().replace("-", "/"),
                "%rideOnTime%" to rsvInfo.rideOnTime.toString().substring(0, 5),
                "%companyNotice%" to rsvInfo.notice,
                "%url%" to "http://localhost:8080/app/sertificates/?id=${rsvInfo.reservationId}",
                "%companyName%" to taxiInfo.companyName,
                "%companyContact%" to taxiInfo.contact
        )

        return createMailCommon(replaceMap, mailType, rsvInfo.passengerMail)
    }

    /**
     * 管理者向けパスワードリセットメールの送信処理
     */
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun sendAccountResetMail(userName: String, password: String, companyName: String, companyMail: String): Boolean {
        lateinit var response: Response
        try {
            // 送信するメールを作成する
            val mail = createAccountResetMail(userName, password, companyName, companyMail)
            val request = Request()
            request.endpoint = "mail/send"
            request.method = Method.POST
            request.body = mail.build()
            // メール送信
            response = sendGrid.api(request)
        } catch (ex: Exception) {
            println(ex.message)
        }
        return response.statusCode in 200..299
    }

    /**
     * 管理者向けパスワードリセットメールの作成処理
     */
    private fun createAccountResetMail(userName: String, password: String, companyName: String, companyMail: String)
            : Mail {

        //送信メール種別設定（必ずリセットメール）
        val mailType = Constants.MAIL_TYPE.RESET

        //メールに埋め込む動的項目のマップ化
        val replaceMap = mutableMapOf(
                "%userName%" to userName,
                "%password%" to password,
                "%companyName%" to companyName
        )

        return createMailCommon(replaceMap, mailType, companyMail)
    }

    /**
     * メール作成処理の共通部
     */
    private fun createMailCommon(replaceMap: MutableMap<String, String>, mailType: Enum<Constants.MAIL_TYPE>, mailAddress: String): Mail {
        // メールテンプレート取得
        var mailMain = Constants.mailContent[mailType] ?: throw Exception()

        // 動的項目の差し替え
        for (e in replaceMap.entries) {
            mailMain = mailMain.replace(e.key, e.value)
        }

        // 送信メール設定
        val from = Email(Constants.FROM_ADDRESS)
        //TODO 開発用誤送信防止のため、アドレス固定
        println("本来送信するメールアドレス：$mailAddress")
        val to = Email("yuuya.s.toyoda@jalinfotec.co.jp"/*mailAddress*/)
        val subject = Constants.mailSubject[mailType] ?: throw Exception()
        val content = Content("text/plain", mailMain)

        return Mail(from, subject, to, content)
    }
}