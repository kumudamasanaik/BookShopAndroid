package com.kumuda.bookshopandroid.screens.changepassword

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse

interface ChangePasswordContract {
    interface View : BaseView {
        fun doChangePassword()
        fun sendOtp()

        fun invalidEmailPhone()
        fun invalidPass()
        fun setChangePasswordResp(res: CommonRes)
        fun setOTPResp(res: SendOtpResponse)

    }

    interface Presenter : BasePresenter<View> {
        fun validateLogin(name: String?, pass: String?): Boolean
        fun doChangePassword(email: String, password: String,otp:String)
        fun doSendOtp(MobileNumber:String)

    }
}