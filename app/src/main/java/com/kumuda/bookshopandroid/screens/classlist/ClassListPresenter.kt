package com.kumuda.bookshopandroid.screens.classlist

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.GetClassList
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class ClassListPresenter(view: ClassListContract.View) : ClassListContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ClassListContract.View? = view

    override fun callGetClassListApi(school_id:String) {
        iResponseInterface.callApi(
            MyApplication.service.getClassList(CommonUtils.getAutharizationKey(),ApiRequestParam.getClassListRequestParameters(school_id)), ApiType.CLASS_LIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.CLASS_LIST ->
                    view?.setClassListResp(response.body() as GetClassList)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.CLASS_LIST ->
                view?.showMsg(responseError.message)
        }
    }
}