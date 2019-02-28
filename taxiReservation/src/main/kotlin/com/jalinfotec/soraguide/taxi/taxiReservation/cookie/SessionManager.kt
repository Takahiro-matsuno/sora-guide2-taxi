package com.jalinfotec.soraguide.taxi.taxiReservation.cookie

import javax.servlet.http.HttpServletRequest

class SessionManager {
    /**
     * セッション生成、予約番号を保持
     *
     * 呼び出し
     *   予約詳細取得処理、変更用予約情報取得処理
     */
    fun setSession(rsvId: String, request: HttpServletRequest) {
        val session = request.session
        session.setAttribute("rsvId", rsvId)
    }

    /**
     * セッションから予約番号を取得する
     *
     * 呼び出し
     *   変更入力画面表示処理、変更処理、取消処理
     */
    fun getSession(request: HttpServletRequest): String {
        val session = request.session

        try {
            return session.getAttribute("LastUpdate") as String
        } catch (e: Exception) {
            println("セッションに最終更新日が存在しない")
            throw Exception()
        }
    }

    /**
     * セッション削除
     *
     * 呼び出し
     *   変更処理、取消処理
     */
    fun deleteSession(request: HttpServletRequest){
        val session = request.session
        session.removeAttribute("rsvId")
    }

}