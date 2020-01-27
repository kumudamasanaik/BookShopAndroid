package com.kumuda.bookshopandroid.screens.notification

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.NotificationResponse
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class NotificationPresenter(view: NotificationContract.View) : NotificationContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: NotificationContract.View? = view

    override fun callGetNotificationApi() {
        iResponseInterface.callApi(
            MyApplication.service.getNotification(CommonUtils.getAutharizationKey(),ApiRequestParam.getNotificationRequestParameters()), ApiType.NOTIFICATIONLIST)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.NOTIFICATIONLIST ->
                    view?.setNotificationResp(response.body() as NotificationResponse)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.NOTIFICATIONLIST ->
                view?.showMsg(responseError.message)
        }
    }
}