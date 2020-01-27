package com.kumuda.bookshopandroid.screens.orderdetail

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.OrderDetailResponseModel

interface OrderDetailsContract {
    interface View : BaseView {
        fun callOrderDetails()
        fun setOrderDetailsResp(res: OrderDetailResponseModel)
    }

    interface Presenter : BasePresenter<View> {
        fun callMyOrderDetailsApi(order_id: String)
    }
}