package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import javax.servlet.http.HttpServletRequest

class SessionManager {
    /**
     * セッション生成、予約番号を保持
     *
     * 呼び出し
     *   予約詳細取得処理
     */
    fun setSession(rsvId: String, request: HttpServletRequest) {
        println("【Session】予約番号セット:$rsvId")
        val session = request.session
        session.setAttribute("rsvId", rsvId)
    }

    /**
     * セッションから予約番号を取得する
     *
     * 呼び出し
     *   変更入力画面表示処理、変更処理、取消処理
     */
    fun checkSession(checkId: String, request: HttpServletRequest): String {
        val session = request.session
        val rsvId: String

        try {
            rsvId = session.getAttribute("rsvId") as String
            println("【Session】予約番号取得:$rsvId")
        } catch (e: Exception) {
            println("セッションに予約番号が存在しない")
            throw Exception()
        }

        //引数のIDとセッションのIDが一致していない場合はエラー（改ざん、ブラウザバック対策）
        if (rsvId != checkId) {
            throw Exception()
        }

        return rsvId
    }

    /**
     * セッション削除
     *
     * 呼び出し
     *   変更処理、取消処理
     */
    fun deleteSession(request: HttpServletRequest) {
        println("【Session】予約番号開放")
        val session = request.session
        session.removeAttribute("rsvId")
    }

}