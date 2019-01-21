package com.jalinfotec.soraguide.taxi.taxiReservation.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create

class TaxiInfoDao {
    fun read(): String {
        //var taxiList: List<String>? = null
        var result = ""

        Database.connect(
                "jdbc:postgresql://taxiapptest-postgresqldbserver.postgres.database.azure.com:5432/taxiapptestdb",
                "org.postgresql.Driver",
                "postgresqldbuser@taxiapptest-postgresqldbserver", "ZAQ12wsx")

        transaction{
            addLogger(StdOutSqlLogger)
            taxi_info.selectAll().forEach {
                result +=
                """{
                    ${it[taxi_info.companyId]},
                    ${it[taxi_info.name]},
                    ${it[taxi_info.contact]},
                    ${it[taxi_info.location]},
                    }

                """
            }
        }
        return if (result.isEmpty()) "Non Result" else result
    }
}


/*
class TaxiInfomationEntity(id:EntityID<String>): Entity<String>(id) {
    companion object : EntityClass<TaxiInfomationEntity>(TaxiInfomationTable)
}
*/

object taxi_info : Table("taxi_info") {
    val companyId = varchar("companyid", 4).primaryKey()
    val name = varchar("name", 20)
    val contact = varchar("contact", 15)
    val location = varchar("location", 30)
}

object Colors: Table("colors") {
    val code = varchar("code", 6).primaryKey()
    val name = varchar("name", 255)
}

object Rgbs: Table("rgbs") {
    val code = (varchar("code", 6) references Colors.code).primaryKey()
    val r = integer("r")
    val g = integer("g")
    val b = integer("b")
}

/*
data class TaxiInformations(
        var companyId: String,
        var name: String,
        var contact: String,
        var location: String
)

*/

