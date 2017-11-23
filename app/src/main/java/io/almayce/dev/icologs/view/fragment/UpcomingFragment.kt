package io.almayce.dev.icologs.view.fragment

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.databinding.FragmentUpcomingBinding
import io.almayce.dev.icologs.presenter.UpcomingPresenter
import io.almayce.dev.icologs.view.ItemActivity
import io.almayce.dev.icologs.view.UpcomingView
import io.almayce.dev.icologs.adapter.UpcomingRecyclerViewAdapter

/**
 * Created by almayce on 28.08.17.
 */


class UpcomingFragment : MvpAppCompatFragment(), UpcomingView, UpcomingRecyclerViewAdapter.ItemClickListener {

    @InjectPresenter
    lateinit var pr: UpcomingPresenter
    private lateinit var bn: FragmentUpcomingBinding
    override fun onResume() {
        super.onResume()
    pr.onResume()
        
    }
    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        bn = DataBindingUtil.inflate<FragmentUpcomingBinding>(
                inflater, R.layout.fragment_upcoming, container, false)
        pr.init(context)

        bn.rvContent.layoutManager = LinearLayoutManager(context)
        bn.rvContent.adapter = pr.adapter
        pr.adapter?.setClickListener(this)

        return bn.getRoot()
    }

    override fun onItemClick(view: View, position: Int) {
        var target = pr.getIco(position)
        var intent = Intent(context, ItemActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("id", target?.id)
        intent.putExtra("title", target?.title)
        intent.putExtra("descript", target?.descript)
        intent.putExtra("status", target?.status)
        intent.putExtra("startDate", target?.startDate)
        intent.putExtra("endDate", target?.endDate)
        intent.putExtra("symbol", target?.symbol)
        intent.putExtra("tokenPrice", target?.tokenPrice)
        intent.putExtra("sum", target?.sum)
        intent.putExtra("invest", target?.invest)
        intent.putExtra("link", target?.link)
        intent.putExtra("whitePaperLink", target?.whitePaperLink)
        intent.putExtra("news", target?.news)
        intent.putExtra("preSaleDate", target?.preSaleDate)
        intent.putExtra("newDate", target?.newDate)
        intent.putExtra("fix", target?.fix)
        intent.putExtra("youTubeUrl", target?.youTubeUrl)

        startActivity(intent)
    }
}