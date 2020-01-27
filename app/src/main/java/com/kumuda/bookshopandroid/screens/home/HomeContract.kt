package com.kumuda.bookshopandroid.screens.home

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.GetOrderResp
import com.kumuda.bookshopandroid.model.realModel.GetProfileDetailResponse

interface HomeContract {
    interface View : BaseView {
        fun getProfile()
        fun setGetProfileResp(res: GetProfileDetailResponse)
        fun getOrder()
        fun setGetOrderResp(res: GetOrderResp)
        /*fun doLogin()
        fun setLoginResp(res: CostomerResp)
*/
    }

    interface Presenter : BasePresenter<View> {
        fun callGetProfileApi(profile_id:String)
        fun getOrderApi()
    }
}