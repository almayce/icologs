package io.almayce.dev.icologs.adapter

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
        val target = list.get(position)

        binding.ivNewsLogo.setImageResource(when {
            target.contains("youtube") -> R.drawable.ic_yt
            target.contains("twitter") -> R.drawable.ic_tw
            target.contains("telegram") -> R.drawable.ic_tg
            target.contains("t.me") -> R.drawable.ic_tg
            target.contains("slack") -> R.drawable.ic_sl
            target.contains("bitcointalk") -> R.drawable.ic_bc
            target.contains("facebook") -> R.drawable.ic_fb
            target.contains("reddit") -> R.drawable.ic_rd
            target.contains("plus.google.com") -> R.drawable.ic_go
            target.contains("vk.com") -> R.drawable.ic_vk
            target.contains("github.com") -> R.drawable.ic_gh
            target.contains("instagram.com") -> R.drawable.ic_in
            target.contains("medium.com") -> R.drawable.ic_me
            else -> R.drawable.ic_ln
        })

        binding.rlNewsItem.contentDescription = list.get(position)
        return binding.root
    }
}
