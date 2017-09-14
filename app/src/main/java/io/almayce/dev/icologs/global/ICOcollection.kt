package io.almayce.dev.icologs.global

import io.almayce.dev.icologs.model.ICO

/**
 * Created by almayce on 28.08.17.
 */
object ICOcollection {
    var instance = ArrayList<ICO>()

    fun getPinICO(set: HashSet<String>): ArrayList<ICO> {

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

    fun getNowICO(): ArrayList<ICO> {
        var target = ArrayList<ICO>()

        for (i in instance)
            if (i.status == "now")
                target.add(i)

        return bubbleSortByEndDate(target)
    }

    fun getUpICO(): ArrayList<ICO> {
        var target = ArrayList<ICO>()

        for (i in instance)
            if (i.status == "up")
                target.add(i)

        return bubbleSortByStartDate(target)
    }

    fun getEndICO(): ArrayList<ICO> {
        var target = ArrayList<ICO>()

        for (i in instance)
            if (i.status == "end")
                target.add(i)

        return bubbleSortByEndDate(target)
    }

    fun bubbleSortByStartDate(input: ArrayList<ICO>): ArrayList<ICO> {

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

    fun bubbleSortByEndDate(input: ArrayList<ICO>): ArrayList<ICO> {

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
}