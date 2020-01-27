package com.kumuda.bookshopandroid.model.inputmodel

data class DeliveryAddressInput(
        val DeliveryMode: String?,
        val Address1: String?,
        val Address2: String?,
        val City: String?,
        val State: String?,
        val PostalCode: String?,
        val MobileNumber: String?,
        val EmailAddress: String?
)