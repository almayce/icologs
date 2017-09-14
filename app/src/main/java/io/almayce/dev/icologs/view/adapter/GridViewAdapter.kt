package io.almayce.dev.icologs.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.databinding.ItemNewsBinding

/**
 * Created by almayce on 30.05.17.
 */

class GridViewAdapter(private val context: Context, var list: ArrayList<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = DataBindingUtil.inflate<ItemNewsBinding>(LayoutInflater.from(context),
                R.layout.item_news, parent, false)
        when {
            list.get(position).contains("youtube") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_yt)
            list.get(position).contains("twitter") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_tw)
            list.get(position).contains("telegram") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_tg)
            list.get(position).contains("t.me") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_tg)
            list.get(position).contains("slack") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_sl)
            list.get(position).contains("bitcointalk") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_bc)
            list.get(position).contains("facebook") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_fb)
            list.get(position).contains("reddit") -> binding.ivNewsLogo.setImageResource(R.drawable.ic_rd)
        }
        binding.rlNewsItem.contentDescription =  list.get(position)
        return binding.root
    }
}
