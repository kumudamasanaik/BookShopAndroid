package com.kumuda.bookshopandroid.screens.orderdetail

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.OrderDetailResponseModel
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class OrderDetailPresenter(view: OrderDetailsContract.View) : OrderDetailsContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: OrderDetailsContract.View? = view


    override fun callMyOrderDetailsApi(order_id: String) {
        iResponseInterface.callApi(MyApplication.service.getOrderDetail(CommonUtils.getAutharizationKey(),
            ApiRequestParam.getMyOrderDetailsParameters(order_id)), ApiType.ORDER_DETAILS)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.ORDER_DETAILS ->
                    view?.setOrderDetailsResp(response.body() as OrderDetailResponseModel)

            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.ORDER_DETAILS ->
                view?.showMsg(responseError.message)

        }
    }
}