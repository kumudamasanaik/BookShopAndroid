package com.kumuda.bookshopandroid.commonadapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kumuda.bookshopandroid.R
import com.kumuda.bookshopandroid.listener.IAdapterClickListener
import com.kumuda.bookshopandroid.util.inflate
import com.kumuda.bookshopandroid.util.withNotNullNorEmpty
import kotlinx.android.extensions.LayoutContainer

class TestRecAdapter(var context: Context, var type: Int, var adapterType: String = "common", var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<TestRecAdapter.ViewHolder>() {
    var list: ArrayList<*>? = null

    fun addList(list: ArrayList<*>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestRecAdapter.ViewHolder {
        var view = parent.inflate(R.layout.partialy_bundle_list_item)
        return ViewHolder(view, adapterClickListener, adapterType)
    }

    override fun getItemCount(): Int {
        return if (list != null && list!!.size > 0) list!!.size else 6
    }

    override fun onBindViewHolder(holder: TestRecAdapter.ViewHolder, position: Int) {
        list.withNotNullNorEmpty {
            holder.bind(context, list!![position], position)
            return
        }
        holder.bind(context, holder, position)

    }
    inner class ViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?, var adapterType: String = "common") : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(context: Context, item: Any, pos: Int) {
            item.apply {

                itemView.setOnClickListener {
                    adapterClickListener!!.onclick(item = item, pos = adapterPosition, type = adapterType,view = containerView)
                }
            }
        }
    }
}