package com.kumuda.bookshopandroid.screens.school

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.GetSchoolResponse
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class SchoolPresenter(view: SchoolContract.View) : SchoolContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: SchoolContract.View? = view

    override fun callGetSchoolApi() {
        iResponseInterface.callApi(
            MyApplication.service.getSchool(CommonUtils.getAutharizationKey()), ApiType.SCHOOL)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.SCHOOL ->
                    view?.setSchoolResp(response.body() as GetSchoolResponse)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.SCHOOL ->
                view?.showMsg(responseError.message)
        }
    }
}