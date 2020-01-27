package com.kumuda.bookshopandroid.screens.changepassword

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import com.kumuda.bookshopandroid.model.realModel.SendOtpResponse
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class ChangePasswordPresenter(view: ChangePasswordContract.View) : ChangePasswordContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ChangePasswordContract.View? = view

    override fun validateLogin(name: String?, pass: String?): Boolean {
        if (name.isNullOrEmpty()) {
            view?.invalidEmailPhone()
            return false
        }
        if (pass.isNullOrEmpty()) {
            view?.invalidPass()
            return false
        }
        return true
    }

    override fun doSendOtp(MobileNumber: String) {
        iResponseInterface.callApi(MyApplication.service.sendOtp(ApiRequestParam.sendOtp(MobileNumber)), ApiType.SEND_OTP)
    }

    override fun doChangePassword(email: String, password: String, otp: String) {
        iResponseInterface.callApi(MyApplication.service.doChangePAssword(CommonUtils.getAutharizationKey(),ApiRequestParam.changePassword(email, password,otp)), ApiType.CHANGE_PASSWORD)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.SEND_OTP ->
                    view?.setOTPResp(response.body() as SendOtpResponse)

                ApiType.CHANGE_PASSWORD ->
                    view?.setChangePasswordResp(response.body() as CommonRes)

            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.SEND_OTP ->
                view?.showMsg(responseError.message)

            ApiType.CHANGE_PASSWORD ->
                view?.showMsg(responseError.message)

        }
    }
}