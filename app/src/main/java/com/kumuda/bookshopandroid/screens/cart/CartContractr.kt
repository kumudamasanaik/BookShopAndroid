package com.kumuda.bookshopandroid.screens.cart

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.model.realModel.CommonRes
import com.kumuda.bookshopandroid.model.realModel.UpdateCartResp

interface CartContractr {
    interface  View:BaseView{
        fun getCart()
        fun setGetCartResp(res:CommonRes)
        fun setRemoveFromCartResp(res: UpdateCartResp, item:BundlePack)

    }
    interface  Presenter:BasePresenter<View>{
        fun callGetCartApi()
        fun callRemoveFromCartApi(item: BundlePack)

    }
}