package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.BookingInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.Numbering
import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.TaxiInformation
import com.jalinfotec.soraguide.taxi.taxiReservation.data.form.ReservationForm
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.BookingInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.NumberingRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.repository.TaxiInfoRepository
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationChangeService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationCompleteService
import com.jalinfotec.soraguide.taxi.taxiReservation.data.service.ReservationDetailService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

/**
 * サンプル用controllerクラス
 * おためし用のクラスのため、後々削除する。
 */
//TODO クラス削除
@Controller
class SampleController(
        private val taxiRepository: TaxiInfoRepository,
        private val bookingRepository: BookingInfoRepository,
        private val numberingRepository: NumberingRepository,
        private val rdb: ReservationDetailService,
        private val rsvCompService: ReservationCompleteService,
        private val rsvDetailservice: ReservationDetailService,
        private val rsvChangeService: ReservationChangeService
) {

    // 疎通確認
    @RequestMapping("hello")
    fun hello(@RequestParam(value = "name") name: String?): String {
        return if (name == null) "who are you?" else "name"
    }

    // データベースアクセス
    @RequestMapping("db")
    fun db(mav: ModelAndView, @RequestParam("id") id: String?): ModelAndView {
        mav.viewName = "/testHTML/dbRead"
        if (id != null) {
            mav.addObject("dataList", taxiRepository.findById(id))
        } else {
            mav.addObject("dataList", taxiRepository.findAll())
        }
        return mav
    }

    // thymeleaf表示
    @RequestMapping("view")
    fun view() {

    }

    // thymeleafにdbデータ埋め込み
    @RequestMapping("dbView")
    fun dbView() {

    }

    @RequestMapping("/")
    @ResponseBody
    fun home(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/reservationList"
        return mav
    }

    @RequestMapping("/login")
    @ResponseBody
    fun login(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/reservationDetail"
        mav.addObject("test", "aaa")
        return mav
    }

    @RequestMapping("/bookingInfo")
    @ResponseBody
    fun detailTest(mav: ModelAndView, @RequestParam("id") id: String): ModelAndView {
        mav.viewName = "testHTML/reservationDetail"
        mav.addObject("dataList", rdb.getDetail(id))
        mav.addObject("status", rdb.statusText)
        mav.addObject("companyName", rdb.taxiCompanyName)
        return mav
    }

    @RequestMapping("/changeTest")
    @ResponseBody
    fun changeTest(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/index"
        val bookInfo = bookingRepository.findById("0000000001").get()
        bookInfo.name = "江戸川コナン"
        bookInfo.phonetic = "エドガワコナン"
        bookingRepository.save(bookInfo)
        return mav
    }

    /**
     * 登録処理テスト用
     */
    @RequestMapping("/reservationTest")
    @ResponseBody
    fun reservationTest(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/registrationTest"
        mav.addObject("taxiList", taxiRepository.findAll())
        mav.addObject("reservationForm", rsvDetailservice.getChangeDetail("0000000003"))

        return mav
    }

    @RequestMapping("/reservationCompTest")
    @ResponseBody
    fun reservationCompTest(mav: ModelAndView,
                            @ModelAttribute("reservationForm") rsvForm: ReservationForm,
                            @ModelAttribute("id") id: String,
                            @ModelAttribute("name") name: String): ModelAndView {

        val checkBooking = bookingRepository.findById(id)
        //検索にかからない場合
        if (!checkBooking.isPresent) {
            println("ERROR：変更する予約がDBに存在しない")
            throw Exception()
        }
        //ID改ざん対策
        if (checkBooking.get().name.trim() != name.trim()) {
            println("ERROR：変更前後の予約で名前が一致しない")
            println("DB->${checkBooking.get().name.trim()}")
            println("入力->${name.trim()}")
            throw Exception()
        }

        //変更処理
        //rsvChangeService.change(id,rsvForm)

        //完了画面の確認は別途
        mav.viewName = "testHTML/login"
        return mav
    }

    @RequestMapping("/bookingSearch")
    @ResponseBody
    fun bookingSearch(mav: ModelAndView): ModelAndView {
        mav.viewName = "list"

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val dateString = sdf.format(java.util.Date())

        val date = Date.valueOf(dateString)
        mav.addObject("rsvList", bookingRepository.findByStatusAndDateGreaterThanEqualOrderByIdAsc(1, date))

        return mav

    }

    //DBデータの初期化用メソッド
    @RequestMapping("/test/setup")
    fun setup(mav: ModelAndView): ModelAndView {
        mav.viewName = "testHTML/allClear"
        taxiRepository.deleteAll()
        bookingRepository.deleteAll()
        numberingRepository.save(Numbering(name = "taxi_info", nextValue = 4))
        numberingRepository.save(Numbering(name = "booking_info", nextValue = 6))

        val taxiInfo1 = TaxiInformation()
        taxiInfo1.id = "0001"
        taxiInfo1.name = "お試しタクシー1"
        taxiInfo1.contact = "08011112222"
        taxiInfo1.location = "高松市"
        val taxiInfo2 = TaxiInformation()
        taxiInfo2.id = "0002"
        taxiInfo2.name = "お試しタクシー2"
        taxiInfo2.contact = "080123456789"
        taxiInfo2.location = "どこか"
        val taxiInfo3 = TaxiInformation()
        taxiInfo3.id = "0003"
        taxiInfo3.name = "文字数が多い場合の表示を確認するためのタクシー会社"
        taxiInfo3.contact = "08011112222"
        taxiInfo3.location = "高松市"
        val list = mutableListOf(taxiInfo1, taxiInfo2, taxiInfo3)
        taxiRepository.saveAll(list)

        val bookingInfo1 = BookingInformation(
                id = "0000000001",
                status = 1,
                date = Date.valueOf("2019-03-04"),
                time = Time.valueOf("10:00:00"),
                adult = 1,
                child = 2,
                taxi_number = 1,
                company_id = "0001",
                destination = "うどん屋さん",
                name = "お試し太郎一号",
                phonetic = "オタメシタロウイチゴウ",
                phone = "08011112222",
                mail = "aaa@jalinfotec.co.jp",
                comment = "コメントテスト1件目",
                car_number = "",
                car_contact = "",
                notice = ""
        )
        val bookingInfo2 = BookingInformation(
                id = "0000000002",
                status = 5,
                date = Date.valueOf("2018-12-31"),
                time = Time.valueOf("22:55:00"),
                adult = 3,
                child = 1,
                taxi_number = 2,
                company_id = "0002",
                destination = "目的地サンプル",
                name = "お試し太郎二号",
                phonetic = "オタメシタロウ二ゴウ",
                phone = "0801234567892",
                mail = "bbb@jalinfotec.co.jp",
                comment = "コメントテスト2件目",
                car_number = "",
                car_contact = "",
                notice = ""
        )
        val bookingInfo3 = BookingInformation(
                id = "0000000003",
                status = 2,
                date = Date.valueOf("2018-12-31"),
                time = Time.valueOf("22:55:00"),
                adult = 3,
                child = 1,
                taxi_number = 2,
                company_id = "0003",
                destination = "目的地はどこだ",
                name = "お試し太郎SAN号",
                phonetic = "オタメシタロウサンゴウ",
                phone = "0801234567893",
                mail = "ccc@jalinfotec.co.jp",
                comment = "コメントテスト3件目",
                car_number = "",
                car_contact = "",
                notice = ""
        )
        val bookingInfo4 = BookingInformation(
                id = "0000000004",
                status = 4,
                date = Date.valueOf("2019-05-05"),
                time = Time.valueOf("12:45:00"),
                adult = 9,
                child = 5,
                taxi_number = 6,
                company_id = "0001",
                destination = "目的地がいっぱい",
                name = "お試し太郎四号",
                phonetic = "オタメシタロウヨンゴウ",
                phone = "0801234567894",
                mail = "ddd@jalinfotec.co.jp",
                comment = "コメントテスト4件目",
                car_number = "",
                car_contact = "",
                notice = ""
        )
        val bookingInfo5 = BookingInformation(
                id = "0000000005",
                status = 3,
                date = Date.valueOf("2020-07-25"),
                time = Time.valueOf("01:35:00"),
                adult = 2,
                child = 0,
                taxi_number = 1,
                company_id = "0003",
                destination = "オリンピックの会場",
                name = "お試し太郎五号",
                phonetic = "オタメシタロウゴゴウ",
                phone = "0801234567895",
                mail = "eee@jalinfotec.co.jp",
                comment = "コメントテスト5件目",
                car_number = "",
                car_contact = "",
                notice = ""
        )
        val bookList = mutableListOf(bookingInfo1, bookingInfo2, bookingInfo3, bookingInfo4, bookingInfo5)
        bookingRepository.saveAll(bookList)

        return mav
    }
}