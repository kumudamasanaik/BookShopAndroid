package com.kumuda.bookshopandroid.screens.registration

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.CostomerResp
import com.kumuda.bookshopandroid.model.inputmodel.RegistrationInput
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse
import com.kumuda.bookshopandroid.util.Validation
import retrofit2.Call
import retrofit2.Response

class RegistrationPresenter(view: RegistrationContract.View) : RegistrationContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: RegistrationContract.View? = view

    override fun validate(input:RegistrationInput): Boolean {
        if (input.ProfileName.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("1")
            return false
        }
        if (!Validation.isValidMobileNumber(input.MobileNumber)) {
            view!!.showSignupValidateErrorMsg("2")
            return false
        }
        if (input.OTP.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("3")
            return false
        }
        if (!Validation.isValidEmail(input.EmailAddress!!)) {
            view!!.showSignupValidateErrorMsg("4")
            return false
        }
        if(!Validation.isValidPassword(input.Password!!)){
            view!!.showSignupValidateErrorMsg("5")
            return false
        }
        return true
    }

    override fun doSendOtp(MobileNumber: String) {
        iResponseInterface.callApi(MyApplication.service.sendOtp(ApiRequestParam.sendOtp(MobileNumber)), ApiType.SEND_OTP)
    }

    override fun doRegister(registerInput: RegistrationInput) {
        iResponseInterface.callApi(MyApplication.service.doRegister(registerInput), ApiType.REGISTER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.SEND_OTP ->
                    view?.setOTPResp(response.body() as SendOtpResponse)
                ApiType.REGISTER ->
                    view?.setRegistrationResp(response.body() as CostomerResp)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.SEND_OTP ->
                view?.showMsg(responseError.message)
            ApiType.REGISTER ->
                view?.showMsg(responseError.message)

        }
    }
}