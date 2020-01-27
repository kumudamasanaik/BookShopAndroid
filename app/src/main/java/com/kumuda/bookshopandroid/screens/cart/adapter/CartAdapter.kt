package com.kumuda.bookshopandroid.screens.cart.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import com.kumuda.bookshopandroid.util.CommonUtils
import com.kumuda.bookshopandroid.util.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_cart_list_item.*

class CartAdapter(var context: Context, var type: Int, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    var list: ArrayList<BundlePack>? = null
    var modifProd: BundlePack? = null
    var temp_cart_value: Int? = null
    lateinit var addbtn: TextView
    lateinit var removebtn: TextView

    fun addList(list: ArrayList<BundlePack>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        var view = parent.inflate(R.layout.partialy_cart_list_item)
        return ViewHolder(view, adapterClickListener, adapterType)
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 0
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        holder.bind(context, list!![position], position)
    }

    inner class ViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var adapterType: String = "common")
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(context: Context, item: Any, pos: Int) {
            item.apply {
                if (item is BundlePack) {
                    tv_cart_item_name.text = item.Name
                    tv_cart_item_rate.text = CommonUtils.getRupeesSymbol(context!!, item.Rate.toString())

                    remove_from_cart.setOnClickListener {
                        adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.REMOVE_FROM_CART, view = containerView)
                    }
                }
            }
        }
    }
}
