package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.entity

import javax.persistence.*

@Entity
@Table(name = "user_info")
class Account(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", nullable = false, unique = true)
        var id: Long = 0L,

        @Column(name = "user_name", nullable = false, unique = true)
        var username: String = "",

        @Column(nullable = false)
        var password: String,

        @Column(name = "enable_flg", nullable = false)
        var enabled: Boolean = true,

        @Column(name = "admin_flg", nullable = false)
        val admin_flg: Boolean,

        @Column(name = "company_id", nullable = false)
        val company_id: String = "0002"
        )