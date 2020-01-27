package com.kumuda.bookshopandroid.screens.myprofile.viewHolder

import android.content.Context
import android.view.View
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.SchoolItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_selected_scool_list_item.*

class ProfileViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {

        item.apply {

            if (item is SchoolItem) {
                    selected_school_name.text = item.Name
                    selected_school_location.text = item.Address
            }

            itemView.setOnClickListener { adapterClickListener!!.onclick(item = item,
                    pos = adapterPosition,
                    type = adapterType,
                    view = containerView
                )
            }

        }

    }
}