package com.kumuda.bookshopandroid.screens.orderdetail.viewholder

import android.content.Context
import android.view.View
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.Result
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_order_detail_list_item.*

class OrderViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {
            if (item is Result){
                tv_ordered_item_name.text=item.Text
                if(item.Rate != null)
                tv_bundle_rate.text=item.Rate.toString()

                /* //tv_order_id.text=item.order_id
                 order_rate.text=item.rate
                 date.text=item.date
                 status.text=item.status
                 address.text=item.delivery_address
                 payment_mode.text=item.payment_mode
                 school_name.text=item.name
                 tv_rate.text=item.name*/

            }
        }
    }
}