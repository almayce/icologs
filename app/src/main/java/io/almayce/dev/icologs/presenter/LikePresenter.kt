package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.global.ICOcollection
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.firebase.DatabaseReader
import io.almayce.dev.icologs.view.LikeView
import io.almayce.dev.icologs.view.adapter.LikeRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class LikePresenter : MvpPresenter<LikeView>() {
    lateinit var adapter: LikeRecyclerViewAdapter
    lateinit var list: ArrayList<ICO>

    fun init(context: Context) {
        list = ICOcollection.getPinICO(PrefHelper.set)
        adapter = LikeRecyclerViewAdapter(context, list)

        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    list.clear()
                    list.addAll(ICOcollection.getPinICO(PrefHelper.set))
                    adapter.notifyDataSetChanged()
                })

        PrefHelper.onPinned
                .compose(SchedulersTransformer())
                .subscribe({
                    list.clear()
                    list.addAll(ICOcollection.getPinICO(PrefHelper.set))
                    adapter.notifyDataSetChanged()
                })
    }

}