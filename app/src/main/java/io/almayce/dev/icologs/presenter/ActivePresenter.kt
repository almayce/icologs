package io.almayce.dev.icologs.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.model.ICOcollection
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.ICO
import io.almayce.dev.icologs.model.DatabaseReader
import io.almayce.dev.icologs.view.ActiveView
import io.almayce.dev.icologs.adapter.ActiveRecyclerViewAdapter
import io.almayce.dev.icologs.global.PrefHelper
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * Created by almayce on 02.09.17.
 */
@InjectViewState
class ActivePresenter : MvpPresenter<ActiveView>() {
    var adapter: ActiveRecyclerViewAdapter? = null

    fun init(context: Context) {
        adapter = ActiveRecyclerViewAdapter(context, ICOcollection.now!!)
        ICOcollection.onSorted
                .compose(SchedulersTransformer())
                .subscribe({
                    adapter?.notifyDataSetChanged()
                })
    }

    fun getList() = ICOcollection.now

    fun getIco(postion: Int): ICO? =
        ICOcollection.now?.get(postion)

 fun onResume() {
    adapter?.notifyDataSetChanged()
}

}