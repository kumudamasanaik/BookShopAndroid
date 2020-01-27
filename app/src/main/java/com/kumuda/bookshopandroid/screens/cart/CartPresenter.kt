package com.kumuda.bookshopandroid.screens.cart

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import com.kumuda.bookshopandroid.model.realModel.UpdateCartResp
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class CartPresenter(view:CartContractr.View):CartContractr.Presenter,IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: CartContractr.View? = view
    private lateinit var bundleItem: BundlePack

    override fun callGetCartApi() {
        iResponseInterface.callApi(MyApplication.service.getCartList(CommonUtils.getAutharizationKey(), ApiRequestParam.getCartRequestParameters()), ApiType.GET_CART)
    }

    override fun callRemoveFromCartApi(item: BundlePack) {
        bundleItem=item
        val package_id=item.ID.toString()
        val quantity=item.quantity.toString()
        iResponseInterface.callApi( MyApplication.service.getUpdateCart(CommonUtils.getAutharizationKey(),
            ApiRequestParam.getUpdateCartRequestParameters(package_id,quantity)), ApiType.REMOVE_FROM_CART)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.GET_CART ->
                    view?.setGetCartResp(response.body() as CommonRes)

                ApiType.REMOVE_FROM_CART ->
                    view?.setRemoveFromCartResp(response.body() as UpdateCartResp,bundleItem)

            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.GET_CART ->
                view?.showMsg(responseError.message)

            ApiType.REMOVE_FROM_CART ->
                view?.showMsg(responseError.message)

        }
    }
}