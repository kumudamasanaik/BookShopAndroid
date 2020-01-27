package com.kumuda.bookshopandroid.screens.myprofile

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.inputmodel.SaveProfileInput
import com.kumuda.bookshopandroid.model.realModel.SaveProfileResp
import com.kumuda.bookshopandroid.model.realModel.GetProfileDetailResponse

interface ProfileContract {
    interface View : BaseView {
        fun getProfile()
        fun saveProfile()
        fun showSignupValidateErrorMsg(msg: String)
        fun setSaveProfileResp(res: SaveProfileResp)
        fun setGetProfileResp(res: GetProfileDetailResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun validate(input: SaveProfileInput): Boolean
        fun callSaveProfileApi(profileInput:SaveProfileInput)
        fun callGetProfileApi(profile_id:String)
    }
}