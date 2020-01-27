package com.kumuda.bookshopandroid.commonadapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.screens.cart.viewholder.CartViewHolder
import com.kumuda.bookshopandroid.screens.home.viewholder.ListViewHolder
import com.kumuda.bookshopandroid.screens.myprofile.viewHolder.ProfileViewHolder
import com.kumuda.bookshopandroid.screens.orderdetail.viewholder.OrderViewHolder
import com.kumuda.bookshopandroid.screens.school.viewHolder.SchoolListViewHolder
import com.kumuda.bookshopandroid.util.inflate
import com.kumuda.bookshopandroid.util.withNotNullNorEmpty

class BaseRecAdapter(var context: Context, var type: Int, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<BaseViewholder>() {

    var list: ArrayList<*>? = null


    fun addList(list: ArrayList<*>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewholder {
        val view = parent.inflate(type)
        lateinit var holder: BaseViewholder
        when (type) {
            R.layout.partialy_notification_list_items -> holder = SchoolListViewHolder(view, adapterType, adapterClickListener)
            R.layout.partialy_scool_list_item -> holder = SchoolListViewHolder(view, adapterType, adapterClickListener)
            R.layout.partialy_class_list_item -> holder = SchoolListViewHolder(view, adapterType, adapterClickListener)
           // R.layout.partialy_bundle_list_item -> holder = BundleListViewHolder(view, adapterType, adapterClickListener, this)
            R.layout.partialy_cart_list_item -> holder = CartViewHolder(view, adapterType, adapterClickListener)
            R.layout.partial_order_list_item -> holder = SchoolListViewHolder(view, adapterType, adapterClickListener)
            R.layout.partialy_order_detail_list_item -> holder = OrderViewHolder(view, adapterType, adapterClickListener)
            R.layout.partialy_selected_scool_list_item -> holder = ProfileViewHolder(view, adapterType, adapterClickListener)
            R.layout.partial_selected_scool_list -> holder = ProfileViewHolder(view, adapterType, adapterClickListener)
            R.layout.item_school_list -> holder = ListViewHolder(view, adapterType, adapterClickListener)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 0
      //  return  list!!.size`
    }

    /* fun updatePostList(postList:ArrayList<*>, add:Boolean=false){

     }*/

    override fun onBindViewHolder(holder: BaseViewholder, position: Int) {
        list.withNotNullNorEmpty {
            holder.bind(context, list!![position], position)
            return
        }
        holder.bind(context, holder, position)
    }
}