package com.kumuda.bookshopandroid.model.realModel

data
class NotificationResponse(
    val Error: String,
    val Items: ArrayList<NotificationItem>,
    val Status: String
)