package com.kumuda.bookshopandroid.screens.home.viewholder

import android.content.Context
import android.view.View
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.SchoolItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_school_list.*

class ListViewHolder(
    override val containerView: View,
    var adapterType: String = "common",
    var adapterClickListener: IAdapterClickListener? = null
) : BaseViewholder(containerView), LayoutContainer {
    private var checkbox = false

    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {


            if (item is SchoolItem) {
                tv_school_name.text=item.Name
                tv_school_location.text=item.Address
            }

            itemView.setOnClickListener {
                adapterClickListener!!.onclick(
                    item = item,
                    pos = adapterPosition,
                    type = adapterType,
                    view = containerView
                )
            }
        }
    }
}