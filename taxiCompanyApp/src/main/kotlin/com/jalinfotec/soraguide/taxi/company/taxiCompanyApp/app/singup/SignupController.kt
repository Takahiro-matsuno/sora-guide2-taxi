package com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.app.singup

import com.jalinfotec.soraguide.taxi.company.taxiCompanyApp.domain.service.UserAccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

@Controller
class SignupController {
    @Autowired
    private lateinit var uas: UserAccountService

    // サインアップ画面表示
    @RequestMapping(value = ["/signup"], method = [RequestMethod.GET])
    fun signup(mav: ModelAndView): ModelAndView {
        mav.viewName = "signup"
        mav.addObject("isError", false)
        return mav
    }
    // サインアップ画面のエラー表示
    @RequestMapping(value = ["/signup-error"])
    fun signupError(mav: ModelAndView): ModelAndView {
        mav.viewName = "signup"
        mav.addObject("isError", true)
        return mav
    }
    // サインアップ処理
    @RequestMapping(value = ["/signup"], method = [RequestMethod.POST])
    fun signupUser(@RequestBody body: String): String {
        println(body)

        val pair = parseBodyData(body)
        return if (pair == null) {
            "forward:signup-error"
        } else {
            val success = uas.registerUser(username = pair.first, password = pair.second)
            // 成功時はログイン画面、失敗時はサインアップ画面を表示する
            if (success) "forward:login-signup" else "forward:signup-error"
        }
    }
    // サインアップのボディデータをパーズする
    private fun parseBodyData(body: String): Pair<String, String>? {
        val staStr = "&username="
        val staPos = body.indexOf(staStr)

        val endStr = "&password="
        val endPos = body.indexOf(endStr)

        val username = body.substring(staPos + staStr.length, endPos)
        val password = body.substring(endPos + endStr.length)

        return if (username == "" || password == "") null else Pair(username, password)
    }
}