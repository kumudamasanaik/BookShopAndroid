package com.kumuda.bookshopandroid.screens.bundlelist

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiRequestParam
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.model.realModel.GetPackageResponse
import com.kumuda.bookshopandroid.model.realModel.UpdateCartResp
import com.kumuda.bookshopandroid.util.CommonUtils
import retrofit2.Call
import retrofit2.Response

class BundleListPresenter(view: BundleListContract.View) : BundleListContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: BundleListContract.View? = view

    private lateinit var bundleItem: BundlePack

    override fun callBundleListApi(class_id:String) {
        iResponseInterface.callApi(
            MyApplication.service.getBundleList(CommonUtils.getAutharizationKey(),ApiRequestParam.getBundleListRequestParameters(class_id)), ApiType.BUNDLE_LIST)
    }

    /*override fun callAddToCartApi(package_id: String, quantity: String) {
        iResponseInterface.callApi( MyApplication.service.getUpdateCart(CommonUtils.getAutharizationKey(),
            ApiRequestParam.getUpdateCartRequestParameters(package_id,quantity)), ApiType.ADD_TO_CART)
    }*/

    override fun callAddToCartApi(item: BundlePack) {
        bundleItem=item
        val package_id=item.ID.toString()
        val quantity=item.quantity.toString()
        iResponseInterface.callApi( MyApplication.service.getUpdateCart(CommonUtils.getAutharizationKey(),
            ApiRequestParam.getUpdateCartRequestParameters(package_id,quantity)), ApiType.ADD_TO_CART)
    }

   /* override fun callRemoveFromCartApi(package_id: String, quantity: String) {
        iResponseInterface.callApi( MyApplication.service.getUpdateCart(CommonUtils.getAutharizationKey(),
            ApiRequestParam.getUpdateCartRequestParameters(package_id,quantity)), ApiType.REMOVE_FROM_CART)
    }*/

    override fun callRemoveFromCartApi(item:BundlePack) {
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
                ApiType.BUNDLE_LIST ->
                    view?.setBundleListResp(response.body() as GetPackageResponse)

                ApiType.ADD_TO_CART -> {
                    view?.setAddToCartResp(response.body() as UpdateCartResp,bundleItem)
                }

                ApiType.REMOVE_FROM_CART ->
                    view?.setRemoveFromCartResp(response.body() as UpdateCartResp,bundleItem)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.BUNDLE_LIST ->
                view?.showMsg(responseError.message)

            ApiType.ADD_TO_CART ->
                view?.showMsg(responseError.message)

            ApiType.REMOVE_FROM_CART ->
                view?.showMsg(responseError.message)
        }
    }
}