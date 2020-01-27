package com.kumuda.bookshopandroid.screens.bundlelist.viewHolder

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kumuda.bookshopandroid.MyApplication
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.commonadapter.BaseRecAdapter
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.BundlePack
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partialy_bundle_list_item.*

class BundleListViewHolder(override val containerView: View,
                           var adapterType: String = "common",
                           var adapterClickListener: IAdapterClickListener? = null,
                           var adpter: BaseRecAdapter? = null) : BaseViewholder(containerView), LayoutContainer {

    private lateinit var source: String

    override fun bind(context: Context, item: Any, pos: Int) {
        /*item.apply {
            add_bundle.setOnClickListener {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.ADD_TO_CART)
            }
        }*/
        if (item is BundlePack){
           // val school: TextView = itemView.findViewById(R.id.school_name)
            school_name.text=item.Name
            tv_bundle_rate.text=item.Rate.toString()
            dec.text=item.Details
        }
        if (item is BundlePack){
            val school: TextView = itemView.findViewById(R.id.school_name)
            school.text=item.Name
            tv_bundle_rate.text=item.Rate.toString()
            dec.text=item.Details
        }
        val addbtn: TextView = itemView.findViewById(R.id.add_bundle)
        val removebtn: TextView = itemView.findViewById(R.id.remove)

        addbtn.tag = item;
        addbtn.setOnClickListener {
            val tag = addbtn.tag;
            if(tag is BundlePack) {
                tag.isAdded = true
               // tag.quantity=1
                //Todo quantity value is changed to 1, means item has to be add to cart
                adpter!!.notifyDataSetChanged()
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.ADD_TO_CART,view = containerView)
                Toast.makeText(MyApplication.context, "added", Toast.LENGTH_LONG).show()

            }

            //removebtn.visibility=View.VISIBLE
            //addbtn.visibility = if (addbtn.visibility === View.VISIBLE){
            //    View.GONE
            //}
            //else
            //    View.VISIBLE
            //adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.ADD_TO_CART,view = containerView)
        }
        removebtn.tag = item;
        removebtn.setOnClickListener {
            val tag = removebtn.tag;
            if(tag is BundlePack) {
                adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.REMOVE_FROM_CART,view = containerView)
                Toast.makeText(MyApplication.context, "removed", Toast.LENGTH_LONG).show()
            }

            //adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.REMOVE_FROM_CART,view = containerView)
           /* removebtn.visibility = if (removebtn.visibility === View.VISIBLE){
                View.GONE
            }
            else
                View.VISIBLE*/
        }

        if(item is BundlePack) {
            if(item.isAdded == true) {
                addbtn.visibility = View.GONE;
                removebtn.visibility = View.VISIBLE;
            }
            else {
                addbtn.visibility = View.VISIBLE;
                removebtn.visibility = View.GONE;
            }
        }
    }
}