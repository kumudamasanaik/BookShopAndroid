package com.kumuda.bookshopandroid.listener

import android.view.View

interface IAdapterClickListener {
    fun onclick(item: Any, pos: Int = 0, type: String? = "none", op: String = "none",view: View )
}