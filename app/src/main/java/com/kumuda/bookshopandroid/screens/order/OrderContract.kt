package com.kumuda.bookshopandroid.screens.order

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.GetOrderResp

interface OrderContract {
    interface View : BaseView {
        fun getOrder()
        fun setGetOrderResp(res: GetOrderResp)
    }

    interface Presenter : BasePresenter<View> {
        fun getOrderApi()
    }
}