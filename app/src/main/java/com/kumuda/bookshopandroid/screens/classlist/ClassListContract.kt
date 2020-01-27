package com.kumuda.bookshopandroid.screens.classlist

import com.freshbasket.customer.screens.BasePresenter
import com.freshbasket.customer.screens.BaseView
import com.kumuda.bookshopandroid.model.realModel.GetClassList

interface ClassListContract {
    interface View : BaseView {
        fun getClassList()
        fun setClassListResp(res: GetClassList)
    }

    interface Presenter : BasePresenter<View> {
        fun callGetClassListApi(school_id:String)

    }
}