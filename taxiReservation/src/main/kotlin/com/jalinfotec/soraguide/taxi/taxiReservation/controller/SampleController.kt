package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.Numbering
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.ReservationInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.NumberingRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.ReservationInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.ModelAndView
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * サンプル用controllerクラス
 * おためし用のクラスのため、後々削除する。
 */
//TODO 開発用 クラス削除
@Controller
class SampleController(
        private val taxiRepository: TaxiInfoRepository,
        private val reservationRepository: ReservationInfoRepository,
        private val numberingRepository: NumberingRepository,
        private val messageSource: MessageSource
) {
    @RequestMapping("/")
    @ResponseBody
    fun home(mav: ModelAndView, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        mav.viewName = "index"

        val locale = request.locale.language
        val lang: String
        lang = when (locale) {
            "ja" -> "日本語"
            "en" -> "英語"
            else -> "日本語でも英語でもない"
        }

        mav.addObject("lang", "$locale ⇒　$lang")
        mav.addObject("isTest", Constants.TEST_MODE)

        return mav
    }

    @RequestMapping("/testError")
    @ResponseBody
    fun error(mav: ModelAndView, request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        mav.viewName = "error"
        mav.addObject("errorMessage", messageSource.getMessage("DBError", null, Locale.JAPAN))

        return mav
    }

    //DBデータの初期化用メソッド
    @RequestMapping("/test/setup")
    fun setup(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/allClear"
        reservationRepository.deleteAll()
        taxiRepository.deleteAll()
        numberingRepository.save(Numbering(tableName = "taxi_info", nextValue = 4))
        numberingRepository.save(Numbering(tableName = "reservation_info", nextValue = 7))

        val taxiInfo1 = TaxiInformation()
        taxiInfo1.id = "0001"
        taxiInfo1.companyName = "お試しタクシー1"
        taxiInfo1.contact = "08011112222"
        taxiInfo1.companyMail = "aaa@jalinfotec.co.jp"
        val taxiInfo2 = TaxiInformation()
        taxiInfo2.id = "0002"
        taxiInfo2.companyName = "お試しタクシー2"
        taxiInfo2.contact = "080123456789"
        taxiInfo2.companyMail = "aaa@jalinfotec.co.jp"
        val taxiInfo3 = TaxiInformation()
        taxiInfo3.id = "0003"
        taxiInfo3.companyName = "お試しタクシー3"
        taxiInfo3.contact = "08011112222"
        taxiInfo3.companyMail = "aaa@jalinfotec.co.jp"
        val list = mutableListOf(taxiInfo1, taxiInfo2, taxiInfo3)
        taxiRepository.saveAll(list)

        val bookingInfo1 = ReservationInformation(
                reservationId = "0000000001",
                status = 1,
                rideOnDate = Date.valueOf("2019-03-04"),
                rideOnTime = Time.valueOf("10:00:00"),
                adult = 1,
                child = 2,
                carDispatchNumber = 1,
                companyId = "0001",
                destination = "うどん屋さん",
                passengerName = "お試し太郎一号",
                passengerPhonetic = "オタメシタロウイチゴウ",
                passengerContact = "0801234567891",
                mail = "aaa@jalinfotec.co.jp",
                comment = "コメントテスト1件目",
                carNumber = "",
                carContact = "",
                notice = "",
                uuid = "",
                lastUpdate = Timestamp.valueOf("2019-01-01 08:00:00")
        )
        val bookingInfo2 = ReservationInformation(
                reservationId = "0000000002",
                status = 2,
                rideOnDate = Date.valueOf("2019-05-01"),
                rideOnTime = Time.valueOf("22:55:00"),
                adult = 3,
                child = 1,
                carDispatchNumber = 2,
                companyId = "0002",
                destination = "目的地サンプル",
                passengerName = "お試し太郎二号",
                passengerPhonetic = "オタメシタロウ二ゴウ",
                passengerContact = "0801234567892",
                mail = "bbb@jalinfotec.co.jp",
                comment = "コメントテスト2件目",
                carNumber = "",
                carContact = "",
                notice = "",
                uuid = "",
                lastUpdate = Timestamp.valueOf("2019-01-01 08:00:00")
        )
        val bookingInfo3 = ReservationInformation(
                reservationId = "0000000003",
                status = 4,
                rideOnDate = Date.valueOf("2019-3-22"),
                rideOnTime = Time.valueOf("22:55:00"),
                adult = 3,
                child = 1,
                carDispatchNumber = 2,
                companyId = "0003",
                destination = "目的地はどこだ",
                passengerName = "お試し太郎SAN号",
                passengerPhonetic = "オタメシタロウサンゴウ",
                passengerContact = "0801234567893",
                mail = "ccc@jalinfotec.co.jp",
                comment = "コメントテスト3件目",
                carNumber = "",
                carContact = "",
                notice = "",
                uuid = "",
                lastUpdate = Timestamp.valueOf("2019-01-01 08:00:00")
        )
        val bookingInfo4 = ReservationInformation(
                reservationId = "0000000004",
                status = 4,
                rideOnDate = Date.valueOf("2019-03-23"),
                rideOnTime = Time.valueOf("12:45:00"),
                adult = 9,
                child = 5,
                carDispatchNumber = 6,
                companyId = "0001",
                destination = "目的地がいっぱい",
                passengerName = "お試し太郎四号",
                passengerPhonetic = "オタメシタロウヨンゴウ",
                passengerContact = "0801234567894",
                mail = "ddd@jalinfotec.co.jp",
                comment = "コメントテスト4件目",
                carNumber = "",
                carContact = "",
                notice = "",
                uuid = "",
                lastUpdate = Timestamp.valueOf("2019-01-01 08:00:00")
        )
        val bookingInfo5 = ReservationInformation(
                reservationId = "0000000005",
                status = 3,
                rideOnDate = Date.valueOf("2019-03-25"),
                rideOnTime = Time.valueOf("01:35:00"),
                adult = 2,
                child = 0,
                carDispatchNumber = 1,
                companyId = "0003",
                destination = "オリンピックの会場",
                passengerName = "お試し太郎五号",
                passengerPhonetic = "オタメシタロウゴゴウ",
                passengerContact = "0801234567895",
                mail = "eee@jalinfotec.co.jp",
                comment = "コメントテスト5件目",
                carNumber = "",
                carContact = "",
                notice = "",
                uuid = "",
                lastUpdate = Timestamp.valueOf("2019-01-01 08:00:00")
        )
        val bookingInfo6 = ReservationInformation(
                reservationId = "0000000006",
                status = 5,
                rideOnDate = Date.valueOf("2019-03-24"),
                rideOnTime = Time.valueOf("01:35:00"),
                adult = 2,
                child = 0,
                carDispatchNumber = 1,
                companyId = "0003",
                destination = "清水ビル",
                passengerName = "お試し太郎六号",
                passengerPhonetic = "オタメシタロウロクゴウ",
                passengerContact = "0801234567896",
                mail = "fff@jalinfotec.co.jp",
                comment = "コメントテスト6件目",
                carNumber = "",
                carContact = "",
                notice = "",
                uuid = "",
                lastUpdate = Timestamp.valueOf("2019-01-01 08:00:00")
        )
        val bookList = mutableListOf(
                bookingInfo1, bookingInfo2, bookingInfo3, bookingInfo4, bookingInfo5, bookingInfo6)
        reservationRepository.saveAll(bookList)

        return mav
    }
}