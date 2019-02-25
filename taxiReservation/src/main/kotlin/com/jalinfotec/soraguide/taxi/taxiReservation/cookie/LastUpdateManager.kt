package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import java.sql.Timestamp
import javax.servlet.http.HttpServletRequest

class LastUpdateManager {
    /**
     * 最終更新日をセッションに保持
     *
     * 呼び出し
     *   予約詳細取得処理、変更用予約情報取得処理
     */
    fun setSession(lastUpdate: Timestamp, request: HttpServletRequest) {
        val session = request.session
        session.setAttribute("LastUpdate", lastUpdate)
    }

    /**
     * 最終更新日をセッションから取得してチェックを行う
     *
     * input
     *   checkTimestamp:チェックしたい値
     *   request:HTTPリクエスト、予約詳細表示時または変更入力画面表示時の最終更新日を保持
     * return
     *   true:チェックOK、変更・取消可能
     *   false:チェックNG、セッションに最終更新日が存在しない、または他の利用者・管理者にて更新が入っている
     * 呼び出し
     *   変更処理、取消処理
     */
    fun checkSession(checkTimestamp: Timestamp, request: HttpServletRequest): Boolean {
        val session = request.session
        val lastUpdate: Timestamp

        try {
            lastUpdate = session.getAttribute("LastUpdate") as Timestamp
        } catch (e: Exception) {
            println("セッションに最終更新日が存在しない")
            return false
        }

        println("最終更新日:$lastUpdate")
        if (lastUpdate == checkTimestamp) {
            return true
        }

        return false
    }
}
