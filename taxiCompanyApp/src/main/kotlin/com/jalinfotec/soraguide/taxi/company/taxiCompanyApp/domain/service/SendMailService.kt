package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import com.sendgrid.*
import org.springframework.stereotype.Service

@Service
class SendMailService(
        private val sendGrid: SendGrid
) {

    fun sendMail(toAddress: String, mailType: Enum<Constants.MAIL_TYPE>): Boolean {
        // 送信するメールを作成する
        val mail = createMail(toAddress, mailType)

        val request = Request()
        lateinit var response: Response
        try {
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

    private fun createMail(toAddress: String, mailType: Enum<Constants.MAIL_TYPE>): Mail {
        val from = Email(Constants.FROM_ADDRESS)
        val to = Email(toAddress)
        val subject = Constants.mailSubject[mailType] ?: throw Exception()
        val content = Content("text/plain",
                Constants.mailContent[mailType] ?: throw Exception())

        return Mail(from, subject, to, content)
    }
}