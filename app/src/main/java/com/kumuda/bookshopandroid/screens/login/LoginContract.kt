package com.kumuda.bookshopandroid.screens.login

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.CostomerResp
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse

interface LoginContract {
    interface View : BaseView {
        fun doLogin()
        fun sendOtp()

        fun invalidEmailPhone()
        fun invalidPass()
        fun setLoginResp(res: CostomerResp)
        fun setOTPResp(res: SendOtpResponse)

    }

    interface Presenter : BasePresenter<View> {
        fun validateLogin(name: String?, pass: String?): Boolean
        fun doLogin(email: String, password: String,otp:String)
        fun doSendOtp(MobileNumber:String)

    }
}