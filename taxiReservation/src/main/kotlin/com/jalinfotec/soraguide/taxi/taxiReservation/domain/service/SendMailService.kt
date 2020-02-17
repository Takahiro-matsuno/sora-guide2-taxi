package com.jalinfotec.soraguide.taxi.taxiReservation.domain.service

import com.jalinfotec.soraguide.taxi.taxiReservation.infrastructure.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.infrastructure.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Environment
import com.sendgrid.*
import org.springframework.stereotype.Service

@Service
class SendMailService(
        private val sendGrid: SendGrid
) {

    /**
     * メール送信処理
     */
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
            println("【ERROR】メール送信エラー：${ex.message}")
        }
        return response.statusCode in 200..299
    }

    /**
     * メール作成処理
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
                "%url%" to "${Environment.SITE_HOST}/app/certificateInput/?id=${rsvInfo.reservationId}",
                "%companyName%" to taxiInfo.companyName,
                "%companyContact%" to taxiInfo.contact
        )

        // メールテンプレート取得
        var mailMain = Constants.mailContent[mailType] ?: throw Exception()

        // 動的項目の差し替え
        for (e in replaceMap.entries) {
            mailMain = mailMain.replace(e.key, e.value)
        }

        // 送信メール設定
        val from = Email(Environment.FROM_ADDRESS)
        //TODO 開発用 誤送信防止のため、アドレス固定
        val to = Email(rsvInfo.mail)
        val subject = Constants.mailSubject[mailType] ?: throw Exception()
        val content = Content("text/plain", mailMain)

        // メール送信処理
        return Mail(from, subject, to, content)
    }
}