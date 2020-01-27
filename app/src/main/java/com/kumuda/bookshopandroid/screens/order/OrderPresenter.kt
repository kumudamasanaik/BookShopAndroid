package com.kumuda.bookshopandroid.screens.order

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.GetOrderResp
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class OrderPresenter(view: OrderContract.View) : OrderContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OrderContract.View? = view


    override fun getOrderApi() {
        iResponseInterface.callApi(MyApplication.service.getOrder(CommonUtils.getAutharizationKey(), ApiRequestParam.getOrderRequestParameters()), ApiType.ORDER)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.ORDER ->
                    view?.setGetOrderResp(response.body() as GetOrderResp)

            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.ORDER ->
                view?.showMsg(responseError.message)

        }
    }
}