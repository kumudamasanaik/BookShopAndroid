package com.kumuda.bookshopandroid.screens.checkout

import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.api.ApiResponsePresenter
import com.kumuda.bookshopandroid.api.ApiType
import com.kumuda.bookshopandroid.api.IResponseInterface
import com.kumuda.bookshopandroid.model.inputmodel.DeliveryAddressInput
import com.kumuda.bookshopandroid.model.realModel.GetClassList
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.Validation
import retrofit2.Call
import retrofit2.Response

class CheckOutPresenter(view: CheckoutContract.View) : CheckoutContract.Presenter, IResponseInterface {
    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: CheckoutContract.View? = view

    override fun validate(input:DeliveryAddressInput): Boolean {
        if (input.Address1.isNullOrEmpty()) {
            view!!.showValidateErrorMsg("1")
            return false
        }
        if (input.Address2.isNullOrEmpty()) {
            view!!.showValidateErrorMsg("2")
            return false
        }
        if (input.City.isNullOrEmpty()) {
            view!!.showValidateErrorMsg("3")
            return false
        }
        if (input.State.isNullOrEmpty()) {
            view!!.showValidateErrorMsg("4")
            return false
        }
        if (input.PostalCode.isNullOrEmpty()) {
            view!!.showValidateErrorMsg("5")
            return false
        }
        if (!Validation.isValidMobileNumber(input.MobileNumber)) {
            view!!.showValidateErrorMsg("6")
            return false
        }
        return true
    }

    override fun callSaveDeliveryAddressApi(deliveryAddressInput: DeliveryAddressInput) {
        iResponseInterface.callApi(MyApplication.service.saveDeliveryAddress(CommonUtils.getAutharizationKey(),deliveryAddressInput), ApiType.DELETE_ADDRESS)

    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful) {
            when (reqType) {
                ApiType.DELETE_ADDRESS ->
                    view?.setSaveDeliveryAddressResp(response.body() as GetClassList)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.DELETE_ADDRESS ->
                view?.showMsg(responseError.message)

        }
    }
}