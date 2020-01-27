package com.kumuda.bookshopandroid.screens.home

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.GetOrderResp
import com.kumuda.bookshopandroid.model.realModel.GetProfileDetailResponse
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class HomePresenter(view: HomeContract.View) : HomeContract.Presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: HomeContract.View? = view

    override fun getOrderApi() {
        iResponseInterface.callApi(MyApplication.service.getOrder(CommonUtils.getAutharizationKey(), ApiRequestParam.getOrderRequestParameters()), ApiType.ORDER)
    }

    override fun callGetProfileApi(profile_id:String) {
        iResponseInterface.callApi(MyApplication.service.getProfile(CommonUtils.getAutharizationKey(),ApiRequestParam.getProfilrParameters(profile_id)), ApiType.GET_PROFILE)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.ORDER ->
                    view?.setGetOrderResp(response.body() as GetOrderResp)

                ApiType.GET_PROFILE ->
                    view?.setGetProfileResp(response.body() as GetProfileDetailResponse)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.ORDER ->
                view?.showMsg(responseError.message)

            ApiType.GET_PROFILE ->
                view?.showMsg(responseError.message)
        }
    }
}