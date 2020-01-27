package com.kumuda.bookshopandroid.screens.notification

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.NotificationResponse

interface NotificationContract {
    interface View : BaseView {
        fun getNotification()
        fun setNotificationResp(res: NotificationResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callGetNotificationApi()

    }
}