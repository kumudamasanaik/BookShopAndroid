package com.kumuda.bookshopandroid.model

data class OrderRes (
    val order_id: String?,
    val rate: String?,
    var date: String?,
    var status: String?,
    var isAdded: Boolean? = false
)