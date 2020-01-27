package com.kumuda.bookshopandroid.screens.school

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.GetSchoolResponse

interface SchoolContract {
    interface View : BaseView {
        fun getSchool()
        fun setSchoolResp(res: GetSchoolResponse)
    }

    interface Presenter : BasePresenter<View> {
        fun callGetSchoolApi()

    }
}