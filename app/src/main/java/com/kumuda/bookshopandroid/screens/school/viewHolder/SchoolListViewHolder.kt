package com.kumuda.bookshopandroid.screens.school.viewHolder

import android.content.Context
import android.util.Log
import android.view.View
import com.kumuda.bookshopandroid.commonadapter.BaseViewholder
import com.kumuda.bookshopandroid.constants.Constants
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.model.realModel.CalssItem
import com.kumuda.bookshopandroid.model.realModel.MyOrderItem
import com.kumuda.bookshopandroid.model.realModel.NotificationItem
import com.kumuda.bookshopandroid.model.realModel.SchoolItem
import com.kumuda.bookshopandroid.util.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_common_item.*
import kotlinx.android.synthetic.main.partialy_class_list_item.*
import kotlinx.android.synthetic.main.partialy_notification_list_items.*
import kotlinx.android.synthetic.main.partialy_scool_list_item.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SchoolListViewHolder(
    override val containerView: View,
    var adapterType: String = "common",
    var adapterClickListener: IAdapterClickListener? = null) : BaseViewholder(containerView), LayoutContainer {

    private var checkbox = false
    var count: Int = 0

    override fun bind(context: Context, item: Any, pos: Int) {
        item.apply {

            /*ClassLIst*/
            if (item is CalssItem) {
                class_name.text = item.Name

                itemView.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.CLASS_LIST, view = containerView)
                }
            }

            /*School LIst*/
            if (item is SchoolItem) {
                school_name.text = item.Name
                school_location.text = item.Address
                checkbox_layout.isChecked=item.isSelected

                checkbox_layout.setOnCheckedChangeListener { buttonView, isChecked ->
                    checkbox_layout.isChecked = if (isChecked) true else false
                    item.isSelected = if (isChecked) true else false
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = Constants.TYPE_SCHOOL_LIST_ADAPTER, view = containerView)
                }
            }

            /*Notification*/
            if (item is NotificationItem) {
                tv_delivery_item.text = item.Text
                //tv_delivery_date.text = item.Date

                val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val output = SimpleDateFormat("MMM dd yyyy  h:mm a")
                var d: Date? = null
                try {
                    d = input.parse(item.Date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val formatted = output.format(d)
                Log.i("DATE", "" + formatted)
                tv_delivery_date.text = formatted
            }

            /*GetOrder List*/
            /* if (item is OrderRes) {
                 tv_order_id.text = item.order_id
                 order_rate.text = item.rate
                 date.text = item.date
                 status.text = item.status

                 itemView.setOnClickListener {
                     adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType, view = containerView)
                 }
             }*/

            /*Setting data to getOrder*/
            if (item is MyOrderItem) {
                tv_order_id.text = item.ID.toString()
                order_rate.text = CommonUtils.getRupeesSymbol(context,item.Total.toString())
                tv_date.text = item.Date.toString()
                status.text = item.Status

                /*setting date format */
                /*  val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                  val output = SimpleDateFormat("MMM dd yyyy  h:mm a")
                  var d: Date? = null
                  try {
                      d = input.parse(item.Date.toString())
                  } catch (e: ParseException) {
                      e.printStackTrace()
                  }
                  val formatted = output.format(d)
                  Log.i("DATE", "" + formatted)
                  tv_date.text = formatted
  */
                itemView.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType, view = containerView)
                }
            }

        }
    }
}