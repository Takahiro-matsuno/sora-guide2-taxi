package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

// TODO ユニーク設定
@Entity
@Table(name = "taxi_info")
class TaxiInformation(
    @Id
    @Column(name="company_id")
    var companyId: String,

    @Column(name = "company_name")
    var companyName: String,

    @Column(name = "contact")
    var contact: String,

    @Column(name = "location")
    var location: String
)