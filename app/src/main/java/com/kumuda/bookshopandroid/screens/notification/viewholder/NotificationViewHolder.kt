package com.kumuda.bookshopandroid.screens.notification.viewholder

import android.content.Context
import android.view.View
import com.kumuda.bookshopandroid.commonadapter.BaseRecAdapter
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import kotlinx.android.extensions.LayoutContainer

class NotificationViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {

        }

    }
}