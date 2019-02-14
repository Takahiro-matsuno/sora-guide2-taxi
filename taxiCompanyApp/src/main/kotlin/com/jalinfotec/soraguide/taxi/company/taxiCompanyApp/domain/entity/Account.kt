package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity

import javax.persistence.*

@Entity
@Table(name = "user_info")
class Account(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", nullable = false, unique = true)
        var userId: Long = 0L,

        @Column(name = "user_name", nullable = false, unique = true)
        var username: String = "",

        @Column(nullable = false)
        var password: String,

        @Column(name = "enable_flg", nullable = false)
        var enableFlg: Boolean = true,

        @Column(name = "adminFlg", nullable = false)
        val adminFlg: Boolean,

        @Column(name = "companyId", nullable = false)
        val companyId: String = "0002"
        )