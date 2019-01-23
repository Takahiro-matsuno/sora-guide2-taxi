package com.jalinfotec.soraguide.taxi.taxiReservation.dao


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class BookingInfoDao(manager: EntityManager) {

    //private var entityManager :EntityManager = manager

    fun connect() {
        //DB接続
        Database.connect(
                "jdbc:postgresql://taxiapptest-postgresqldbserver.postgres.database.azure.com:5432/taxiapptestdb",
                "org.postgresql.Driver",
                "postgresqldbuser@taxiapptest-postgresqldbserver", "ZAQ12wsx")
    }

    fun getAll(): String {
        //返却用
        var result = ""

        //DB接続
        connect()

        /*
        var query =entityManager.createQuery("from TaxiInfoItem")
        val list = query.resultList
        entityManager.close()
        return list
        */

        //SELECT
        transaction {
            addLogger(StdOutSqlLogger)
            Booking_info.selectAll().forEach {
                result += """{
                    ${it[Booking_info.booking_id]},
                    ${it[Booking_info.status]},
                    ${it[Booking_info.date]},
                    ${it[Booking_info.adult]},
                    ${it[Booking_info.child]},
                    ${it[Booking_info.taxi_number]},
                    ${it[Booking_info.company_id]},
                    ${it[Booking_info.destination]},
                    ${it[Booking_info.name]},
                    ${it[Booking_info.phonetic]},
                    ${it[Booking_info.phone]},
                    ${it[Booking_info.mail]},
                    ${it[Booking_info.comment]},
                    ${it[Booking_info.car_number]},
                    ${it[Booking_info.car_contact]},
                    ${it[Booking_info.notice]}
                    }"""
            }
        }
        return if (result.isEmpty()) "Non Result" else result
    }

    /*
    fun read(id: String): String {
        //取得結果返却用の変数
        var result = ""

        //DB接続
        Database.connect(
                "jdbc:postgresql://taxiapptest-postgresqldbserver.postgres.database.azure.com:5432/taxiapptestdb",
                "org.postgresql.Driver",
                "postgresqldbuser@taxiapptest-postgresqldbserver", "ZAQ12wsx")

        //SELECT
        transaction {
            addLogger(StdOutSqlLogger)
            result = Booking_info.select {
                Booking_info.booking_id == id
            }
            {
                result +=
                        """{
                    ${it[Booking_info.booking_id]},
                    ${it[Booking_info.status]},
                    ${it[Booking_info.date]},
                    ${it[Booking_info.adult]},
                    ${it[Booking_info.child]},
                    ${it[Booking_info.taxi_number]},
                    ${it[Booking_info.company_id]},
                    ${it[Booking_info.destination]},
                    ${it[Booking_info.name]},
                    ${it[Booking_info.phonetic]},
                    ${it[Booking_info.phone]},
                    ${it[Booking_info.mail]},
                    ${it[Booking_info.comment]},
                    ${it[Booking_info.car_number]},
                    ${it[Booking_info.car_contact]},
                    ${it[Booking_info.notice]}
                    }"""
            }
        }
        return if (result.isEmpty()) "Non Result" else result
    }*/
}

object Booking_info : Table("Booking_info") {
    val booking_id = varchar("booking_id", 10).primaryKey()
    val status = integer("status")
    val date = date("date")
    //val time = date("time")
    val adult = integer("adult")
    val child = integer("child")
    val taxi_number = integer("taxi_number")
    val company_id = varchar("company_id", 4)
    val destination = varchar("destination", 30)
    val name = varchar("name", 20)
    val phonetic = varchar("phonetic", 30)
    val phone = varchar("phone", 15)
    val mail = varchar("mail", 50)
    val comment = varchar("comment", 20)
    val car_number = varchar("car_number", 10)
    val car_contact = varchar("car_contact", 15)
    val notice = varchar("notice", 99)
}

