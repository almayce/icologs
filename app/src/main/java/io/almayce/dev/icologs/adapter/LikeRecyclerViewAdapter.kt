package io.almayce.dev.icologs.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.model.ICO
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by almayce on 20.07.17.
 */

class LikeRecyclerViewAdapter(val context: Context, var list: ArrayList<ICO>) : RecyclerView.Adapter<LikeRecyclerViewAdapter.ViewHolder>() {

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

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val layoutParamsHide = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        val rlNewLayoutParams = holder.rlNew.getLayoutParams()
        val rlPreSaleLayoutParams = holder.rlPreSale.getLayoutParams()

        holder.rlNew.layoutParams = layoutParamsHide
        holder.rlPreSale.layoutParams = layoutParamsHide

        if (isNew(target.newDate)) {
            holder.rlNew.visibility = View.VISIBLE
            holder.rlNew.layoutParams = layoutParams
        } else {
            holder.rlNew.visibility = View.INVISIBLE
            rlNewLayoutParams.width = 0
        }

        if (isPreSale(target.preSaleDate)) {
            holder.rlPreSale.visibility = View.VISIBLE
            holder.rlPreSale.layoutParams = layoutParams
        } else {
            holder.rlPreSale.visibility = View.INVISIBLE
            rlPreSaleLayoutParams.width = 0
        }
//        holder.rlNew.visibility = if (isNew(target.newDate)) View.VISIBLE else View.INVISIBLE
//        holder.rlNew.layoutParams.width = if (isNew(target.newDate)) ViewGroup.LayoutParams.WRAP_CONTENT else 0
//        holder.rlPreSale.visibility = if (isPreSale(target.preSaleDate)) View.VISIBLE else View.INVISIBLE
//        holder.rlPreSale.layoutParams.width = if (isPreSale(target.preSaleDate)) ViewGroup.LayoutParams.WRAP_CONTENT else 0
        holder.ivHeart.visibility = if (PrefHelper.set.contains(target.id)) View.VISIBLE else View.INVISIBLE
        holder.tvTitle.text = target.title
        holder.tvDescript.text = target.descript


        when {
            target.status == "up" -> {
                holder.rlBackground.setBackgroundResource(R.color.status_up)
                holder.tvPrimary.text = statusUpDateInit(list.get(position))[0]
                holder.tvSecondary.text = statusUpDateInit(list.get(position))[1]
                holder.rlDate.visibility = View.VISIBLE
                holder.rlEnd.visibility = View.INVISIBLE
            }
            target.status == "now" -> {
                holder.rlBackground.setBackgroundResource(R.color.status_now)
                holder.tvPrimary.text = statusNowDateInit(list.get(position))[0]
                holder.tvSecondary.text = statusNowDateInit(list.get(position))[1]
                holder.rlDate.visibility = View.VISIBLE
                holder.rlEnd.visibility = View.INVISIBLE
            }
            target.status == "end" -> {
                holder.rlBackground.setBackgroundResource(R.color.status_end)
                holder.rlDate.visibility = View.INVISIBLE
                holder.rlEnd.visibility = View.VISIBLE
            }
        }

    }

    fun isPreSale(millis: String): Boolean =
            millis.toLong() > 0
//                    && System.currentTimeMillis() < millis.toLong()
    fun isNew(millis: String): Boolean = millis.toLong() > 0
            && TimeUnit.HOURS.convert(System.currentTimeMillis() - millis.toLong(), TimeUnit.MILLISECONDS).toInt() < 72

    fun statusUpDateInit(target: ICO): Array<String> {
        val sdfUp = SimpleDateFormat("dd.MM", Locale.ROOT)
        sdfUp.timeZone = TimeZone.getTimeZone("UTC")

        var c = System.currentTimeMillis()
        var s = target.startDate.toLong()
        var e = target.endDate.toLong()
        var array = arrayOf("00", "00")

        array[0] = sdfUp.format(Date(s)).split(".")[0]
        var month = sdfUp.format(Date(s)).split(".")[1]

        array[1] = when(month) {
            "01" -> context.resources.getString(R.string.january)
            "02" -> context.resources.getString(R.string.february)
            "03" -> context.resources.getString(R.string.march)
            "04" -> context.resources.getString(R.string.april)
            "05" -> context.resources.getString(R.string.may)
            "06" -> context.resources.getString(R.string.june)
            "07" ->  context.resources.getString(R.string.july)
            "08" -> context.resources.getString(R.string.august)
            "09" -> context.resources.getString(R.string.september)
            "10" -> context.resources.getString(R.string.october)
            "11" -> context.resources.getString(R.string.november)
            "12" -> context.resources.getString(R.string.december)
            else -> context.resources.getString(R.string.january)
        }

        return array
    }

    fun statusNowDateInit(target: ICO): Array<String> {
        val sdfNow = SimpleDateFormat("DD", Locale.ROOT)
        sdfNow.timeZone = TimeZone.getTimeZone("UTC")

        var c = System.currentTimeMillis()
        var s = target.startDate.toLong()
        var e = target.endDate.toLong()
        var array = arrayOf("00", "00")

        array[0] = sdfNow.format(Date(e - c))
        array[1] = context.resources.getString(R.string.days)

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
        var rlNew: RelativeLayout
        var rlPreSale: RelativeLayout
        var ivHeart: ImageView
        var tvTitle: TextView
        var tvDescript: TextView
        var rlBackground: RelativeLayout
        var rlDate: RelativeLayout
        var tvPrimary: TextView
        var tvSecondary: TextView
        var rlEnd: RelativeLayout

        init {
            rlNew = itemView.findViewById<RelativeLayout>(R.id.rlNew)
            rlPreSale = itemView.findViewById<RelativeLayout>(R.id.rlPreSale)
            ivHeart = itemView.findViewById<ImageView>(R.id.ivHeart)
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
