package com.kumuda.bookshopandroid.screens.bundlelist.adapter

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
import kotlinx.android.synthetic.main.partialy_bundle_list_item.*

class BundleListAdapter(var context: Context, var type: Int, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) :
    RecyclerView.Adapter<BundleListAdapter.ViewHolder>() {

    var list: ArrayList<BundlePack>? = null
    var modifProd: BundlePack? = null
    var temp_cart_value: Int? = null
    lateinit var addbtn:TextView
    lateinit var removebtn:TextView

    fun addList(list: ArrayList<BundlePack>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = parent.inflate(R.layout.partialy_bundle_list_item)
        return ViewHolder(view, adapterClickListener, adapterType)
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list!![position], position)
    }

    inner class ViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var adapterType: String = "common") : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(context: Context, item: Any, pos: Int) {

            if (item is BundlePack) {
                school_name.text = item.Name
                tv_bundle_rate.text = item.Rate.toString()
                dec.text = item.Details
            }

            if (item is BundlePack) {
                val school: TextView = itemView.findViewById(R.id.school_name)
                school.text = item.Name
                tv_bundle_rate.text = CommonUtils.getRupeesSymbol(context, item.Rate.toString())
                dec.text = item.Details
            }
            val addbtn: TextView = itemView.findViewById(R.id.add_bundle)
            val removebtn: TextView = itemView.findViewById(R.id.remove)

            addbtn.tag = item
            addbtn.setOnClickListener {
                val tag = addbtn.tag
                if (tag is BundlePack) {
                    tag.isAdded = true
                    tag.quantity = 1
                    //Todo quantity value is changed to 1, means item has to be add to cart
                    notifyDataSetChanged()
                    adapterClickListener!!.onclick(
                        item = item,
                        pos = adapterPosition,
                        type = Constants.ADD_TO_CART,
                        view = containerView
                    )
                    // Toast.makeText(MyApplication.context, "added", Toast.LENGTH_LONG).show()
                }
            }

            removebtn.tag = item
            removebtn.setOnClickListener {
                val tag = removebtn.tag
                if (tag is BundlePack) {
                    adapterClickListener!!.onclick(
                        item = item,
                        pos = adapterPosition,
                        type = Constants.REMOVE_FROM_CART,
                        view = containerView
                    )
                    //Toast.makeText(MyApplication.context, "removed", Toast.LENGTH_LONG).show()
                }
            }

            /*Visibility of buttons based on checking saved Orderd LIst in CommonUtils*/
            if (!((CommonUtils.getCartList()?.size==0 ) ||(CommonUtils.getCartList().isNullOrEmpty()))) {
                    CommonUtils.getCartList()!!.forEach {
                        if (item is BundlePack) {
                        if (item.ID == it.ID) {
                            removebtn.visibility = View.VISIBLE
                            addbtn.visibility = View.GONE

                        } else {
                            removebtn.visibility = View.GONE
                            addbtn.visibility = View.VISIBLE
                        }
                         }
                     }
                }
            else {
                    removebtn.visibility = View.GONE
                    addbtn.visibility = View.VISIBLE

            }
                /****************************/
               /*  if (item is BundlePack) {
                 if (item.isAdded==true) {
                     addbtn.visibility = View.GONE
                     removebtn.visibility = View.VISIBLE
                 } else {
                     addbtn.visibility = View.VISIBLE
                     removebtn.visibility = View.GONE
                 }
             }*/
            }
    }
}