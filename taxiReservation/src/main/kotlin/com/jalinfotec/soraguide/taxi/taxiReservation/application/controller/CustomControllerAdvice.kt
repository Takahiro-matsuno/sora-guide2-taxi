package com.jalinfotec.soraguide.taxi.taxiReservation.application.controller

import org.hibernate.exception.JDBCConnectionException
import org.springframework.context.MessageSource
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.ModelAndView
import java.util.*

/**
 * 例外処理用のコントローラクラス
 */
@ControllerAdvice
class CustomControllerAdvice(private val messageSource: MessageSource) {
    /**
     * Exceptionのハンドラメソッド
     */
    @ExceptionHandler
    fun handleError(e: Exception): ModelAndView {
        println("【ERROR】${e.cause}")

        val mav = ModelAndView()
        val errorMessage: String = if (e.cause is JDBCConnectionException) {
            messageSource.getMessage("DBError", null, Locale.JAPAN)
        } else {
            messageSource.getMessage("otherError", null, Locale.JAPAN)
        }

        mav.viewName = "error"
        mav.addObject("errorMessage", errorMessage)
        return mav
    }
}