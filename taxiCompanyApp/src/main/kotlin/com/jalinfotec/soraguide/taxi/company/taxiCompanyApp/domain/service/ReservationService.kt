package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.ReservationRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.TaxiCompanyRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Service
class ReservationService(
        private val rsvRepository: ReservationRepository,
        private val taxiRepository: TaxiCompanyRepository,
        private val sendMailService: SendMailService
) {

    // 予約情報フォームの一覧を取得する
    @Transactional(readOnly = true)
    fun getListDefault(companyId: String): ArrayList<ReservationForm> {

        val formList = ArrayList<ReservationForm>()

        if (!taxiRepository.findById(companyId).isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return formList
        }

        // 予約情報一覧取得
        val results = rsvRepository.findByCompanyIdOrderByRideOnDateAscRideOnTimeAsc(companyId)

        // 予約情報を予約情報フォームに変換
        for (rsvInfo in results) {
            val rsvForm = convertRsvInfo2RsvForm(rsvInfo) ?: continue
            formList.add(rsvForm)
        }

        return formList
    }

    // 予約情報フォームと選択可能な予約ステータス一覧を取得
    @Transactional(readOnly = true)
    fun getDetail(companyId: String, reservationId: String): Pair<ReservationForm, ArrayList<String>>? {

        if (!taxiRepository.findById(companyId).isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return null
        }

        // DBから対象の予約情報を取得する
        val rsvInfo = rsvRepository.findByCompanyIdAndReservationId(companyId, reservationId) ?: return null

        // 予約情報を予約情報フォームへ変換
        val rsvForm = convertRsvInfo2RsvForm(rsvInfo) ?: return null

        // 選択可能なステータスの一覧を取得する
        val statusList = getStatusList() ?: return null

        return Pair(rsvForm, statusList)
    }

    // 予約更新
    @Transactional
    fun updateDetail(companyId: String, rsvForm: ReservationForm): Boolean {

        val taxiInfoOptional = taxiRepository.findById(companyId)

        if (!taxiInfoOptional.isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return false
        }

        // 予約情報を取得する
        val preInfo = rsvRepository.findByCompanyIdAndReservationId(companyId, rsvForm.reservationId) ?: return false
        val preInfoStatus = preInfo.status
        val preInfoNotice = preInfo.notice

        if (preInfo.lastUpdate != rsvForm.lastUpdate) {
            // 更新時刻が一致しない場合は処理終了
            println("更新時刻の不一致")
            return false
        }

        // 予約情報フォームを予約情報に変換する
        val aftInfo = convertRsvForm2RsvInfo(preInfo, rsvForm) ?: return false

        // DBを更新
        rsvRepository.save(aftInfo)

        //ステータスの更新があった場合、メール送信判定
        var mailType = Constants.MAIL_TYPE.NONE

        println("変更前ステータス:$preInfoStatus 変更後ステータス${aftInfo.status}")
        if (preInfoStatus != aftInfo.status) {
            if ((preInfoStatus == 1 || preInfoStatus == 3) && aftInfo.status == 2) {
                // 予約確定
                mailType = Constants.MAIL_TYPE.RESERVE
            } else if (aftInfo.status == 5) {
                // 取消確定
                mailType = Constants.MAIL_TYPE.CANCEL
            }
        }

        // タクシー会社からのご案内事項に変更があった場合はメール送信
        if (mailType == Constants.MAIL_TYPE.NONE) {
            if (preInfoNotice != aftInfo.notice) {
                mailType = Constants.MAIL_TYPE.NOTICE
            }
        }

        // メール送信処理
        if (mailType != Constants.MAIL_TYPE.NONE) {
            if (sendMailService.sendMail(aftInfo, taxiInfoOptional.get(), mailType)) {
                println("メール送信完了")
            } else {
                println("メール送信失敗")
            }
        } else {
            println("メール送信無し")
        }

        return true
    }

    // 予約情報をフォームへ変換
    private fun convertRsvInfo2RsvForm(rsvInfo: ReservationInformation): ReservationForm? {

        // 予約ステータスの置き換え
        val statusName = Constants.reservationStatus[rsvInfo.status] ?: return null

        return ReservationForm(
                rsvInfo.reservationId.trim(),
                statusName,
                rsvInfo.rideOnDate,
                rsvInfo.rideOnTime,
                rsvInfo.adult,
                rsvInfo.child,
                rsvInfo.carDispatchNumber,
                rsvInfo.destination.trim(),
                rsvInfo.passengerName.trim(),
                rsvInfo.passengerPhonetic.trim(),
                rsvInfo.passengerContact.trim(),
                rsvInfo.passengerMail.trim(),
                rsvInfo.comment.trim(),
                rsvInfo.carNumber.trim(),
                rsvInfo.carContact.trim(),
                rsvInfo.notice.trim(),
                rsvInfo.lastUpdate)
    }

    // フォームを予約情報へ変換
    private fun convertRsvForm2RsvInfo(rsvInfo: ReservationInformation, rsvForm: ReservationForm): ReservationInformation? {

        // ステータスの置き換え
        rsvInfo.status = -1 // ありえない値を設定
        for (m in Constants.reservationStatus) {
            if (rsvForm.statusName == m.value) {
                rsvInfo.status = m.key
                break
            }
        }
        // -1の場合は変換失敗
        if (rsvInfo.status == -1) return null

        // 予約情報を上書きする
        rsvInfo.rideOnDate = rsvForm.rideOnDate
        rsvInfo.rideOnTime = rsvForm.rideOnTime
        rsvInfo.adult = rsvForm.adult
        rsvInfo.child = rsvForm.child
        rsvInfo.carDispatchNumber = rsvForm.carDispatchNumber
        rsvInfo.destination = rsvForm.destination.trim()
        rsvInfo.passengerName = rsvForm.passengerName.trim()
        rsvInfo.passengerPhonetic = rsvForm.passengerPhonetic.trim()
        rsvInfo.passengerContact = rsvForm.passengerContact.trim()
        rsvInfo.passengerMail = rsvForm.passengerMail.trim()
        rsvInfo.comment = rsvForm.comment.trim()
        rsvInfo.carNumber = rsvForm.carNumber.trim()
        rsvInfo.carContact = rsvForm.carContact.trim()
        rsvInfo.notice = rsvForm.notice.trim()
        rsvInfo.lastUpdate = Timestamp(System.currentTimeMillis())

        return rsvInfo
    }

    // 表示可能なステータス一覧を取得する
    private fun getStatusList(): ArrayList<String>? {

        val list = ArrayList<String>()

        //管理画面では全てのステータスを選択できるようにする。
        Constants.reservationStatus.forEach {
            list.add(it.value)
        }

        return if (list.any()) list else null
    }
}