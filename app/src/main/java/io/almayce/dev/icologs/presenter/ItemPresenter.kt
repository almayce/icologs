package io.almayce.dev.icologs.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.view.ItemView
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by almayce on 29.08.17.
 */
@InjectViewState
class ItemPresenter : MvpPresenter<ItemView>() {

    var utc: TimeZone
    var gmt: TimeZone
    val sdfTimer = SimpleDateFormat("DD HH mm ss", Locale.ROOT)
    val sdfTime = SimpleDateFormat("HH:mm", Locale.ROOT)
    val sdfDate = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)

    init {
        utc = TimeZone.getTimeZone("UTC")
        gmt = TimeZone.getTimeZone("GMT")
        sdfTimer.setTimeZone(utc)
        sdfTime.setTimeZone(utc)
        sdfDate.setTimeZone(utc)
    }

    fun init() {
        PrefHelper.onPinned
                .compose(SchedulersTransformer())
                .subscribe({ t ->
                    if (t) viewState.pin()
                    else viewState.unpin()
                })
    }

    fun setDate(status: String, startDate: String, endDate: String) {

        var s = startDate.toLong()
        var e = endDate.toLong()

        viewState.setDate(
                sdfDate.format(convert(s)),
                sdfDate.format(convert(e)),
                sdfTime.format(convert(s)),
                sdfTime.format(convert(e)))

//        var sOffSet = utc.getOffset(s)
//        var eOffSet = utc.getOffset(e)
//
//        var calStart = Calendar.getInstance(gmt)
//        calStart.timeInMillis = s
//        calStart.add(Calendar.MILLISECOND, sOffSet)
//
//        var calEnd = Calendar.getInstance(gmt)
//        calEnd.timeInMillis = e
//        calEnd.add(Calendar.MILLISECOND, eOffSet)

        when {
            status == "now" -> launchTimer(e)
            status == "up" -> launchTimer(s)
        }
    }

    fun convert(millis: Long): Long {
        val calCurrent = Calendar.getInstance()
        calCurrent.timeInMillis = millis
        calCurrent.add(Calendar.MILLISECOND,
                calCurrent.timeZone.getOffset(calCurrent.timeInMillis))
        return calCurrent.time.time
    }

    fun launchTimer(millis: Long) {
        launch(UI) {
            while (true) {
//                val calCurrent = Calendar.getInstance()
//                calCurrent.add(Calendar.MILLISECOND,
//                        calCurrent.timeZone.getOffset(calCurrent.timeInMillis))
//                val dateCurrent = calCurrent.time

                val t = millis - System.currentTimeMillis()

                if (t < 0)
                    viewState.setTimer(arrayOf("00", "00", "00", "00"))
                else
                    viewState.setTimer(sdfTimer.format(t)
                            .split(" ")
                            .toTypedArray())

                delay(1, TimeUnit.SECONDS)
            }
        }
    }
}