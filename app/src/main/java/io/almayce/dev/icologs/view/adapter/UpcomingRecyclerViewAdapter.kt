package io.almayce.dev.icologs.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.model.ICO
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by almayce on 20.07.17.
 */

class UpcomingRecyclerViewAdapter(val context: Context, var list: ArrayList<ICO>) : RecyclerView.Adapter<UpcomingRecyclerViewAdapter.ViewHolder>() {

    private val inflater: LayoutInflater
    private var clickListener: ItemClickListener? = null
    private var longClickListener: ItemLongClickListener? = null


    init {
        this.inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_ico, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the textview in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val target = list.get(position)

        holder.tvTitle.text = target.title
        holder.tvDescript.text = target.descript
        holder.rlBackground.setBackgroundResource(R.color.status_up)
        holder.tvPrimary.text = statusUpDateInit(list.get(position))[0]
        holder.tvSecondary.text = statusUpDateInit(list.get(position))[1]

    }

    fun statusUpDateInit(target: ICO): Array<String> {
        val sdfUp = SimpleDateFormat("dd.MM", Locale.ROOT)
        sdfUp.timeZone = TimeZone.getTimeZone("UTC")

        var c = System.currentTimeMillis()
        var s = target.startDate.toLong()
        var e = target.endDate.toLong()
        var array = arrayOf("00", "00")

        array[0] = sdfUp.format(Date(s)).split(".")[0]
        var month = sdfUp.format(Date(s)).split(".")[1]

        when {
            month == "01" -> array[1] = context.resources.getString(R.string.january)
            month == "02" -> array[1] = context.resources.getString(R.string.february)
            month == "03" -> array[1] = context.resources.getString(R.string.march)
            month == "04" -> array[1] = context.resources.getString(R.string.april)
            month == "05" -> array[1] = context.resources.getString(R.string.may)
            month == "06" -> array[1] = context.resources.getString(R.string.june)
            month == "07" -> array[1] = context.resources.getString(R.string.july)
            month == "08" -> array[1] = context.resources.getString(R.string.august)
            month == "09" -> array[1] = context.resources.getString(R.string.september)
            month == "10" -> array[1] = context.resources.getString(R.string.october)
            month == "11" -> array[1] = context.resources.getString(R.string.november)
            month == "12" -> array[1] = context.resources.getString(R.string.december)
        }

        return array
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    // total number of rows
    override fun getItemCount(): Int {
        return list.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var tvTitle: TextView
        var tvDescript: TextView
        var rlBackground: RelativeLayout
        var rlDate: RelativeLayout
        var tvPrimary: TextView
        var tvSecondary: TextView
        var rlEnd: RelativeLayout

        init {
            tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            tvDescript = itemView.findViewById<TextView>(R.id.tvDescript)
            rlBackground = itemView.findViewById<RelativeLayout>(R.id.rlBackground)
            rlDate = itemView.findViewById<RelativeLayout>(R.id.rlDate)
            tvPrimary = itemView.findViewById<TextView>(R.id.tvPrimary)
            tvSecondary = itemView.findViewById<TextView>(R.id.tvSecondary)
            rlEnd = itemView.findViewById<RelativeLayout>(R.id.rlEnd)
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(view: View) {
            if (clickListener != null) clickListener!!.onItemClick(view, adapterPosition)
        }

        override fun onLongClick(view: View): Boolean {
            view.contentDescription = tvTitle.text.toString()
            if (longClickListener != null) longClickListener!!.onItemLongClick(view, adapterPosition)
            return true
        }
    }

    // convenience method for getting data at click position
    fun getItem(id: Int): String? {
        return null
    }

    // allows clicks events to be caught
    fun setClickListener(itemClickListener: ItemClickListener) {
        this.clickListener = itemClickListener
    }

    fun setLongClickListener(itemLongClickListener: ItemLongClickListener) {
        this.longClickListener = itemLongClickListener
    }

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface ItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }
}
