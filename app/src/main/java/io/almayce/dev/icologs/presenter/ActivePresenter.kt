package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.global.ICOcollection
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.firebase.DatabaseReader
import io.almayce.dev.icologs.view.ActiveView
import io.almayce.dev.icologs.view.adapter.ActiveRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class ActivePresenter : MvpPresenter<ActiveView>() {
    lateinit var adapter: ActiveRecyclerViewAdapter
    lateinit var list:ArrayList<ICO>

    fun init(context: Context) {
        list =ICOcollection.getNowICO()
        adapter = ActiveRecyclerViewAdapter(context, list)
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    list.clear()
                    list.addAll(ICOcollection.getNowICO())
                    adapter.notifyDataSetChanged()
                })
    }

}