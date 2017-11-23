package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.model.ICOcollection
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.DatabaseReader
import io.almayce.dev.icologs.view.RecentView
import io.almayce.dev.icologs.adapter.RecentRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class RecentPresenter : MvpPresenter<RecentView>() {
    var adapter: RecentRecyclerViewAdapter? = null

    fun init(context: Context) {
        adapter = RecentRecyclerViewAdapter(context, ICOcollection.end!!)
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    adapter?.notifyDataSetChanged()
                })
    }
    fun getIco(postion: Int) =
            ICOcollection.end?.get(postion)
    fun onResume() {
        adapter?.notifyDataSetChanged()
    }
}