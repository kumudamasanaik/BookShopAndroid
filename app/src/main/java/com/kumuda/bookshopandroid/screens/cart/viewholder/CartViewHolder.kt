package com.kumuda.bookshopandroid.screens.cart.viewholder

import android.content.Context
import android.view.View
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_cart_list_item.*

class CartViewHolder(override val containerView: View, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {

            if (item is BundlePack) {
                tv_cart_item_name.text = item.Name
                tv_cart_item_rate.text =
                    CommonUtils.getRupeesSymbol(context!!, item.Rate.toString())

                itemView.setOnClickListener {
                    adapterClickListener!!.onclick(
                        item = item,
                        pos = adapterPosition,
                        type = adapterType,
                        view = containerView
                    )
                }
                remove_from_cart.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.REMOVE_FROM_CART, view = containerView)
                }
            }
        }
    }
}