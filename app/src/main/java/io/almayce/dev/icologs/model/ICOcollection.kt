package io.almayce.dev.icologs.model

import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.reactivex.subjects.PublishSubject

/**
 * Created by almayce on 28.08.17.
 */
object ICOcollection {
    var instance = ArrayList<ICO>()
    var pin = ArrayList<ICO>()
    var now = ArrayList<ICO>()
    var up = ArrayList<ICO>()
    var end = ArrayList<ICO>()

    val onSorted = PublishSubject.create<Boolean>()
    val onPinListRefreshed = PublishSubject.create<Boolean>()

    init {
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    pin.clear()
                    pin.addAll(getPinICO(PrefHelper.set))
                    now.clear()
                    now.addAll(getNowICO())
                    up.clear()
                    up.addAll(getUpICO())
                    end.clear()
                    end.addAll(getEndICO())
                    onSorted.onNext(true)
                })

        PrefHelper.onPinned
                .compose(SchedulersTransformer())
                .subscribe({
                    pin.clear()
                    pin.addAll(getPinICO(PrefHelper.set))
                    onPinListRefreshed.onNext(true)
                })
    }

    fun getStringArrayList(): ArrayList<String> {
        val target = ArrayList<String>()
        for (s in instance)
            target.add(s.title)
        return target
    }

    private fun getPinICO(set: HashSet<String>): ArrayList<ICO> {

        var list = ArrayList<ICO>()
        list.addAll(getNowICO())
        list.addAll(getUpICO())
        list.addAll(getEndICO())

        var target = ArrayList<ICO>()
        for (i in list)
            if (set.contains(i.id))
                target.add(i)

        return target
    }

    private fun getNowICO(): ArrayList<ICO> {
        var target = ArrayList<ICO>()

        for (i in instance)
            if (i.status == "now")
                target.add(i)

        return bubbleSortByFixStatus(bubbleSortNowICO(target))
    }

    private fun getUpICO(): ArrayList<ICO> {
        var target = ArrayList<ICO>()

        for (i in instance)
            if (i.status == "up")
                target.add(i)

        return bubbleSortByFixStatus(bubbleSortUpICO(target))
    }

    private fun getEndICO(): ArrayList<ICO> {
        var target = ArrayList<ICO>()

        for (i in instance)
            if (i.status == "end")
                target.add(i)

        return bubbleSortEndICO(target)
    }

    private fun bubbleSortUpICO(input: ArrayList<ICO>): ArrayList<ICO> {

        val target = input.toTypedArray()
        val n = target.size
        var temp = ICO()

        for (i in 0..n - 1) {
            for (j in 1..n - i - 1) {

                if (target[j - 1].startDate.toLong() > target[j].startDate.toLong()) {
                    temp = target[j - 1]
                    target[j - 1] = target[j]
                    target[j] = temp
                }

            }
        }

        val output = ArrayList<ICO>()
        output.addAll(target)
        return output
    }

    private fun bubbleSortNowICO(input: ArrayList<ICO>): ArrayList<ICO> {

        val target = input.toTypedArray()
        val n = target.size
        var temp = ICO()

        for (i in 0..n - 1) {
            for (j in 1..n - i - 1) {

                if (target[j - 1].endDate.toLong() > target[j].endDate.toLong()) {
                    temp = target[j - 1]
                    target[j - 1] = target[j]
                    target[j] = temp
                }

            }
        }

        val output = ArrayList<ICO>()
        output.addAll(target)
        return output
    }

    private fun bubbleSortEndICO(input: ArrayList<ICO>): ArrayList<ICO> {

        val target = input.toTypedArray()
        val n = target.size
        var temp = ICO()

        for (i in 0..n - 1) {
            for (j in 1..n - i - 1) {

                if (target[j - 1].endDate.toLong() < target[j].endDate.toLong()) {
                    temp = target[j - 1]
                    target[j - 1] = target[j]
                    target[j] = temp
                }

            }
        }

        val output = ArrayList<ICO>()
        output.addAll(target)
        return output
    }

    private fun bubbleSortByFixStatus(input: ArrayList<ICO>): ArrayList<ICO> {

        val target = input.toTypedArray()
        val n = target.size
        var temp = ICO()

        for (i in 0..n - 1) {
            for (j in 1..n - i - 1) {

                if (target[j - 1].fix.toInt() < target[j].fix.toInt()) {
                    temp = target[j - 1]
                    target[j - 1] = target[j]
                    target[j] = temp
                }

            }
        }

        val output = ArrayList<ICO>()
        output.addAll(target)
        return output
    }
}