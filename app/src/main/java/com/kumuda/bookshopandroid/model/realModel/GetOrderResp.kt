package com.kumuda.bookshopandroid.model.realModel

data class GetOrderResp(
    val Error: String,
    val Items: ArrayList<MyOrderItem>,
    val Status: String
)