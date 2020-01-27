package com.kumuda.bookshopandroid.model.inputmodel

data class SaveProfileInput(
        val ProfileID: String?,
        val ProfileName: String?,
        val EmailAddress: String?,
        val SchoolID: String?,
        val ClassID: String?,
        val Address1: String?,
        val Address2: String?,
        val City: String?,
        val State: String?,
        val PostalCode: String?
)
