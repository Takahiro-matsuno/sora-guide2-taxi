package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import com.jalinfotec.soraguide.taxi.taxiReservation.utils.Constants
import javax.servlet.http.HttpServletRequest

class UserAgentManager {
    /**
     * スマホアプリ判定クラス
     */
    fun checkAndroidApp(request: HttpServletRequest): Boolean {
        //UserAgentでアプリのWebViewから開いているかどうか判定
        val userAgent = request.getHeader("user-agent")
        //TODO 開発用
        return Constants.TEST_MODE || userAgent.indexOf("sora-GuideApp") > 0
    }
}