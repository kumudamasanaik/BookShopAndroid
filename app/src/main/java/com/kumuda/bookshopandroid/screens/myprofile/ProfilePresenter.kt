package com.kumuda.bookshopandroid.screens.myprofile

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.inputmodel.SaveProfileInput
import com.kumuda.bookshopandroid.model.realModel.GetProfileDetailResponse
import com.kumuda.bookshopandroid.model.realModel.SaveProfileResp
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class ProfilePresenter(view: ProfileContract.View) : ProfileContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ProfileContract.View? = view

    override fun validate(input:SaveProfileInput): Boolean {
        if (input.ProfileName.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("1")
            return false
        }
        if (input.SchoolID.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("2")
            return false
        }
        if (input.ClassID.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("3")
            return false
        }
        if (input.Address1.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("4")
            return false
        }
        if (input.Address2.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("5")
            return false
        }
        if (input.City.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("6")
            return false
        }
        if (input.State.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("7")
            return false
        }
        if (input.PostalCode.isNullOrEmpty()) {
            view!!.showSignupValidateErrorMsg("8")
            return false
        }
        return true
    }

    override fun callGetProfileApi(profile_id:String) {
        iResponseInterface.callApi(MyApplication.service.getProfile(CommonUtils.getAutharizationKey(),ApiRequestParam.getProfilrParameters(profile_id)), ApiType.GET_PROFILE)
    }

    override fun callSaveProfileApi(profileInput: SaveProfileInput) {
        iResponseInterface.callApi(MyApplication.service.saveProfile(CommonUtils.getAutharizationKey(),profileInput), ApiType.SAVE_PROFILE)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.GET_PROFILE ->
                    view?.setGetProfileResp(response.body() as GetProfileDetailResponse)

                ApiType.SAVE_PROFILE ->
                    view?.setSaveProfileResp(response.body() as SaveProfileResp)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.GET_PROFILE ->
                view?.showMsg(responseError.message)

            ApiType.SAVE_PROFILE ->
                view?.showMsg(responseError.message)

        }
    }
}