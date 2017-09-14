package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.global.ICOcollection
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.firebase.DatabaseReader
import io.almayce.dev.icologs.view.UpcomingView
import io.almayce.dev.icologs.view.adapter.UpcomingRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class UpcomingPresenter : MvpPresenter<UpcomingView>() {
    lateinit var adapter: UpcomingRecyclerViewAdapter
    lateinit var list: ArrayList<ICO>

    fun init(context: Context) {
        list = ICOcollection.getUpICO()
        adapter = UpcomingRecyclerViewAdapter(context, list)
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    list.clear()
                    list.addAll(ICOcollection.getUpICO())
                    adapter.notifyDataSetChanged()
                })
    }

}