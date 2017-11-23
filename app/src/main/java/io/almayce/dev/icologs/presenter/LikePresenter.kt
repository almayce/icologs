package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.model.ICOcollection
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.DatabaseReader
import io.almayce.dev.icologs.view.LikeView
import io.almayce.dev.icologs.adapter.LikeRecyclerViewAdapter

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class LikePresenter : MvpPresenter<LikeView>() {
    var adapter: LikeRecyclerViewAdapter? = null

    fun init(context: Context) {
        adapter = LikeRecyclerViewAdapter(context, ICOcollection.pin)

        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    adapter?.notifyDataSetChanged()
                })

        ICOcollection.onPinListRefreshed
                .compose(SchedulersTransformer())
                .subscribe({
                    adapter?.notifyDataSetChanged()
                })
    }
    fun getIco(postion: Int) =
            ICOcollection.pin?.get(postion)
    fun onResume() {
        adapter?.notifyDataSetChanged()
    }

}