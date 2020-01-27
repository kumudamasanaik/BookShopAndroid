package com.kumuda.bookshopandroid.model

data class OrdeDetailRes (
    val name: String?,
    val rate: String?,
    val item_total: String?,
    val delivery_charge: String?,
    val total:String,
    val order_id: String?,
    var date: String?,
    var status: String?,
    var delivery_address: String?,
    var payment_mode: String?,
    var isAdded: Boolean? = false
    )