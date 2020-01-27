package com.kumuda.bookshopandroid.listener

interface SelectedListClickListener {
    fun onclick(item: Any, type: String = "none", op: String = "none")
}