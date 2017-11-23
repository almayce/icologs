package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.model.ICOcollection
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.DatabaseReader
import io.almayce.dev.icologs.view.UpcomingView
import io.almayce.dev.icologs.adapter.UpcomingRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class UpcomingPresenter : MvpPresenter<UpcomingView>() {
    var adapter:UpcomingRecyclerViewAdapter? = null

    fun init(context: Context) {
        adapter = UpcomingRecyclerViewAdapter(context, ICOcollection.up!!)
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    adapter?.notifyDataSetChanged()
                })
    }

    fun getIco(postion: Int) =
            ICOcollection.up?.get(postion)

    fun onResume() {
        adapter?.notifyDataSetChanged()
    }

}