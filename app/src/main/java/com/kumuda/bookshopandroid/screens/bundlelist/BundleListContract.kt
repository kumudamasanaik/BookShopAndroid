package com.kumuda.bookshopandroid.screens.bundlelist

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.model.realModel.GetPackageResponse
import com.kumuda.bookshopandroid.model.realModel.UpdateCartResp

interface BundleListContract {
    interface View : BaseView {
        fun getBundleList()
        fun setBundleListResp(res: GetPackageResponse)
        fun setAddToCartResp(res: UpdateCartResp,item:BundlePack)
        fun setRemoveFromCartResp(res: UpdateCartResp,item:BundlePack)
    }

    interface Presenter : BasePresenter<View> {
        fun callBundleListApi(class_id:String)
       // fun callAddToCartApi(package_id:String,quantity:String)
        fun callAddToCartApi(item: BundlePack)
       // fun callRemoveFromCartApi(package_id:String,quantity:String)
        fun callRemoveFromCartApi(item: BundlePack)
    }
}