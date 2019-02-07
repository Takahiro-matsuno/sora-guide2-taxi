package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.reservation

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.UserAccount
import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.ReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ReservationRestController {

    @Autowired
    lateinit var reservationService: ReservationService

    @RequestMapping(value = ["/reservation/update"], method = [RequestMethod.GET])
    fun updateNotice(
            @AuthenticationPrincipal user: UserAccount
    ): String {
        // 認証ユーザーから会社IDを取得する
        val companyId = user.getCompanyId()

        /**
         * TODO Cookieに最終アクセス日付を付加する
         * TODO 予約テーブルに更新日を追加
         * TODO 予約テーブルから会社IDで検索をかけて条件に合致するレコード数を返却（company_id = :会社ID and update_date > 最終アクセス日付)
         */
        // sample
        val staStr = "{ \"update_num\": "
        val endStr = "}"
        val cnt = 2

        return "$staStr$cnt$endStr"
    }
}