package com.jalinfotec.soraguide.taxi.taxiReservation.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class TaxiInfoDao {
    fun read() {
        //var taxiList: List<String>? = null

        Database.connect(
                "jdbc:postgresql:taxiapptest-postgresqldbserver.postgres.database.azure.com",
                "org.postgresql.Driver",
                "postgresqldbuser@taxiapptest-postgresqldbserver", "ZAQ12wsx")

        transaction {
            println(taxi_info.select{
                taxi_info.companyId eq "0001"
            }.toString())
        }
    }
}

/*
class TaxiInfomationEntity(id:EntityID<String>): Entity<String>(id) {
    companion object : EntityClass<TaxiInfomationEntity>(TaxiInfomationTable)

}
*/

object taxi_info : Table() {
    val companyId = varchar("companyId", 4).primaryKey()
    val name = varchar("name", 20)
    val contact = varchar("contact", 15)
    val location = varchar("location", 30)
}


/*
data class TaxiInformations(
        var companyId: String,
        var name: String,
        var contact: String,
        var location: String
)

*/

