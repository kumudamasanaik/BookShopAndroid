package com.kumuda.bookshopandroid.screens.registration

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.CostomerResp
import com.kumuda.bookshopandroid.model.inputmodel.RegistrationInput
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse

interface RegistrationContract {
    interface View : BaseView {
        fun doRegister()
        fun sendOtp()
        fun showSignupValidateErrorMsg(msg: String)

        fun setRegistrationResp(res: CostomerResp)
        fun setOTPResp(res: SendOtpResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(input:RegistrationInput): Boolean
        fun doRegister(registerInput:RegistrationInput)
        fun doSendOtp(MobileNumber:String)
    }
}