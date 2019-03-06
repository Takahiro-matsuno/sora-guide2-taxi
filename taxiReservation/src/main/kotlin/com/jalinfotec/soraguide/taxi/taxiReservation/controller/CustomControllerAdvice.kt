package com.jalinfotec.soraguide.taxi.taxiReservation.controller

import org.springframework.cglib.proxy.UndeclaredThrowableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.HashMap
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.ModelAndView
import java.sql.SQLException
import kotlin.reflect.jvm.internal.impl.protobuf.CodedOutputStream


@ControllerAdvice
class CustomControllerAdvice {
    @ExceptionHandler(Exception::class)
    fun handleError(e: Exception): ModelAndView {
        println("きゃっち")
        println(e.message)
        val mav = ModelAndView()
        mav.viewName = "error"
        mav.addObject("errorMassage", e.message ?: "エラーだよー")
        return mav
    }
}