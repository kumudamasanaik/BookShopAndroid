package com.kumuda.bookshopandroid.screens.checkout

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.inputmodel.DeliveryAddressInput
import com.kumuda.bookshopandroid.model.realModel.GetClassList

interface CheckoutContract {
    interface View : BaseView {
        fun doSaveDeliveryAddress()
        fun setSaveDeliveryAddressResp(res: GetClassList)
        fun showValidateErrorMsg(msg: String)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(input:DeliveryAddressInput): Boolean
        fun callSaveDeliveryAddressApi(deliveryAddressInput: DeliveryAddressInput)
    }
}