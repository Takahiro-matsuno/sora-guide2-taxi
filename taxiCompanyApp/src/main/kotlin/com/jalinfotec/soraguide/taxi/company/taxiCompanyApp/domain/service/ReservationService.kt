package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.form.ReservationForm
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.repository.ReservationRepository
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.utils.Constants
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReservationService(
        private val repository: ReservationRepository
) {

    // 予約一覧を取得する
    @Transactional(readOnly = true)
    fun getListDefault(companyId: String): ArrayList<ReservationForm> {

        val results = repository.findByCompanyIdOrderByRideOnDateAscRideOnTimeAsc(companyId)
        val formList = ArrayList<ReservationForm>()

        for (rsvInfo in results) {
            val rsvForm = convertRsvInfo2RsvForm(rsvInfo) ?: continue
            formList.add(rsvForm)
        }
        return formList
    }

    // 予約詳細取得
    @Transactional(readOnly = true)
    fun getDetail(companyId: String, reservationId: String): Pair<ReservationForm, ArrayList<String>>? {
        // DBから対象の予約情報を取得する
        val rsvInfo = repository.findByCompanyIdAndReservationId(companyId, reservationId) ?: return null
        // 予約情報を予約情報フォームへ変換
        val rsvForm = convertRsvInfo2RsvForm(rsvInfo) ?: return null
        // 予約情報のステータスに合わせて、選択可能なステータスの一覧を取得する
        val statusList = getStatusList(rsvInfo.status) ?: return null
        return Pair(rsvForm, statusList)
    }

    // 予約更新
    @Transactional
    fun updateDetail(rsvForm: ReservationForm): Boolean {

        val optional = repository.findById(rsvForm.reservationId)

        if (optional.isPresent) {
            val rsvInfo = convertRsvForm2RsvInfo(optional.get(), rsvForm) ?: return false
            // DB更新
            repository.save(rsvInfo)
            return true
        } else return false
    }

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
                rsvInfo.notice.trim())
    }
    private fun convertRsvForm2RsvInfo(rsvInfo: ReservationInformation, rsvForm: ReservationForm): ReservationInformation? {

        // ステータスの置き換え
        rsvInfo.status = -1
        for (m in Constants.reservationStatus) {
            if (rsvForm.statusName == m.value) {
                rsvInfo.status = m.key
                break
            }
        }
        // ステータスの変換に失敗した場合はNullを返して終了
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

        return rsvInfo
    }

    // 表示可能なステータス一覧を取得する
    private fun getStatusList(status: Int): ArrayList<String>? {

        val list = ArrayList<String>()
        for (i in status until Constants.reservationStatus.keys.size) {
            list.add(Constants.reservationStatus[i]!!)
        }
        return if (list.any()) list else null
    }


/*
    */
    /*

    @Transactional
    fun update(reservation: ReservationInformation): Boolean {
        return true
    }
    */
}