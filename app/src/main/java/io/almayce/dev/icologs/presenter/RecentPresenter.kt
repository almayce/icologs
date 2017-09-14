package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.global.ICOcollection
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.firebase.DatabaseReader
import io.almayce.dev.icologs.view.RecentView
import io.almayce.dev.icologs.view.adapter.RecentRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class RecentPresenter : MvpPresenter<RecentView>() {
    lateinit var adapter: RecentRecyclerViewAdapter
    lateinit var list: ArrayList<ICO>

    fun init(context: Context) {
        list = ICOcollection.getEndICO()
        adapter = RecentRecyclerViewAdapter(context, list)
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    list.clear()
                    list.addAll(ICOcollection.getEndICO())
                    adapter.notifyDataSetChanged()
                })
    }
}