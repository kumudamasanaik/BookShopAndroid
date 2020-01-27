package com.kumuda.bookshopandroid.model.realModel

data class BundlePack(
    val Details: String,
    val ID: Int,
    val Name: String,
    val Rate: Double,
    var isAdded: Boolean = false, //Todo i added extra isAdded parameter.

    var quantity: Int=-1 //Todo i added extra quantity parameter to check add or remove item.
    )