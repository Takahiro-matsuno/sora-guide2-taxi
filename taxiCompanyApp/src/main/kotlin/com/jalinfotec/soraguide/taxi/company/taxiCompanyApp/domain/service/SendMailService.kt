package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import com.sendgrid.*
import org.springframework.stereotype.Service

@Service
class SendMailService(
        private val sendGrid: SendGrid
) {

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

    private fun createMail(rsvInfo: ReservationInformation, taxiInfo: TaxiInformation, mailType: Enum<Constants.MAIL_TYPE>): Mail {
        var mailMain = Constants.mailContent[mailType] ?: throw Exception()
        mailMain = mailMain.replace("-name-", rsvInfo.passengerName)
        mailMain = mailMain.replace("-rsvId-", rsvInfo.reservationId)
        mailMain = mailMain.replace("-rideOnDate-", rsvInfo.rideOnDate.toString().replace("-", "/"))
        mailMain = mailMain.replace("-rideOnTime-", rsvInfo.rideOnTime.toString())
        mailMain = mailMain.replace("-url-", "http://localhost:8080/app/sertificates/?id=${rsvInfo.reservationId}")
        mailMain = mailMain.replace("-companyName-", taxiInfo.companyName)
        mailMain = mailMain.replace("-companyContact-", taxiInfo.contact)


        val from = Email(Constants.FROM_ADDRESS)
        val to = Email(rsvInfo.passengerMail)
        val subject = Constants.mailSubject[mailType] ?: throw Exception()
        val content = Content("text/plain", mailMain)

        return Mail(from, subject, to, content)
    }
}