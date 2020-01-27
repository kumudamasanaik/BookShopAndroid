package com.kumuda.bookshopandroid.model.realModel

data class OrderDetailResponseModel(
    val Address: String,
    val CourierCharge: Int,//Todo i didd Int but it was Any
    val Date: String,
    val DeliveryMode: String,
    val EmailAddress: String,
    val GrandTotal: Any,
    val ID: Int,
    val Items: ArrayList<Result>,
    val MobileNumber: String,
    val OrderRef: Any,
    val Status: String,
    val Total: Any //Todo i didd Int but it was Any
)