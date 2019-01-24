package com.jalinfotec.soraguide.taxi.taxiReservation.data.dao

import com.jalinfotec.soraguide.taxi.taxiReservation.data.entity.TaxiInformation
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


@Repository
interface TaxiInfoDao : JpaRepository<TaxiInformation, Long> {
    fun findByName(name: String): MutableList<TaxiInformation>
}
/*
private var entityManager: EntityManager = manager

fun getAll(): MutableList<Any?>? {


    //取得結果返却用の変数
    //var result = ""
    //var otameshi: MutableList<TaxiInformation> = mutableListOf()

    /*
    //DB接続
    Database.connect(
            "jdbc:postgresql://taxiapptest-postgresqldbserver.postgres.database.azure.com:5432/taxiapptestdb",
            "org.postgresql.Driver",
            "postgresqldbuser@taxiapptest-postgresqldbserver", "ZAQ12wsx")
            */

    var query = entityManager.createQuery("from TaxiInformation")
    val list = query.resultList
    entityManager.close()
    return list
    /*

    //SELECT
    transaction {
        addLogger(StdOutSqlLogger)
        taxi_info.selectAll().forEach {
            result +=
                    """{
                ${it[taxi_info.company_id]},
                ${it[taxi_info.name]},
                ${it[taxi_info.contact]},
                ${it[taxi_info.location]}
                }"""
            /*
            otameshi.add(
                    TaxiInformation(
                            company_id = it[taxi_info.company_id],
                            name = it[taxi_info.name],
                            contact = it[taxi_info.contact],
                            location = it[taxi_info.location]
                    )
            )*/
        }
    }

    //return if (result.isEmpty()) "Non Result" else result

    return  result
    */
}
}

/*
object taxi_info : Table("taxi_info") {
    val company_id = varchar("company_id", 4).primaryKey()
    val name = varchar("name", 20)
    val contact = varchar("contact", 15)
    val location = varchar("location", 30)
}
*/
* */
