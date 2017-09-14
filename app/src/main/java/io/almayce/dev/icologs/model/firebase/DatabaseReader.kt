package io.almayce.dev.icologs.model.firebase

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.almayce.dev.icologs.global.ICOcollection
import io.almayce.dev.icologs.model.ICO
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created by almayce on 28.08.17.
 */

object DatabaseReader {

    private var ref: DatabaseReference
    private var stoRef: StorageReference

    val onReaded = PublishSubject.create<Boolean>()

    init {
        ref = FirebaseDatabase.getInstance().getReference()
        stoRef = FirebaseStorage.getInstance().getReference()
        read()
    }

    fun read() {
        var target = ICO()
        var counter = 0

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(o: DataSnapshot) {
                ICOcollection.instance.clear()
                for (oo: DataSnapshot in o.children) {
                    for (ooo: DataSnapshot in oo.children) {
                        var key = ooo.key
                        var s = String::class.java
                        when {
                            key == "title" -> target.title = ooo.getValue(s)!!
                            key == "descript" -> target.descript = ooo.getValue(s)!!
                            key == "startDate" -> target.startDate = ooo.getValue(s)!!
                            key == "endDate" -> target.endDate = ooo.getValue(s)!!
                            key == "symbol" -> target.symbol = ooo.getValue(s)!!
                            key == "tokenPrice" -> target.tokenPrice = ooo.getValue(s)!!
                            key == "sum" -> target.sum = ooo.getValue(s)!!
                            key == "invest" -> target.invest = ooo.getValue(s)!!
                            key == "link" -> target.link = ooo.getValue(s)!!
                            key == "whitePaperLink" -> target.whitePaperLink = ooo.getValue(s)!!
                            key == "news" -> target.news = getNewsArrayList(ooo.children)
                        }

                        counter++
                        if (counter == 11) {

                            var lang = Locale.getDefault().getDisplayLanguage()
                            var titleArray = target.title.split("<split>")
                            var descriptArray = target.descript.split("<split>")

                            if (lang.contains("русский", true)) {
                                target.title = titleArray[0]
                                target.descript = descriptArray[0]
                            } else if (titleArray.size > 1 && descriptArray.size > 1) {
                                target.title = titleArray[1]
                                target.descript = descriptArray[1]
                            }

                            target.id = oo.key
                            target.status = setStatus(target.startDate, target.endDate)
                            ICOcollection.instance.add(target)
                            target = ICO()
                            counter = 0
                        }
                    }
                    onReaded.onNext(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun getNewsArrayList(snapshot: MutableIterable<DataSnapshot>): ArrayList<String?> {
        var target = ArrayList<String?>()
        for (o: DataSnapshot in snapshot)
            target.add(o.getValue(String::class.java))
        return target
    }

    fun setStatus(startDate: String, endDate: String): String {
        var s = startDate.toLong()
        var e = endDate.toLong()
        var c = System.currentTimeMillis()

        var utc = TimeZone.getTimeZone("UTC")
        var gmt = TimeZone.getTimeZone("GMT")

        var sOffSet = utc.getOffset(s)
        var eOffSet = utc.getOffset(e)

        var calStart = Calendar.getInstance(gmt)
        calStart.timeInMillis = s
        calStart.add(Calendar.MILLISECOND, sOffSet)

        var calEnd = Calendar.getInstance(gmt)
        calEnd.timeInMillis = e
        calEnd.add(Calendar.MILLISECOND, eOffSet)

        var calCurrent = Calendar.getInstance(gmt)
        calCurrent.timeInMillis = c

        var start = calStart
        var end = calEnd
        var current = calCurrent

        var status = "now"

        if (start > current)
            status = "up"
        else if (start < current && end > current)
            status = "now"
        else if (end < current)
            status = "end"

        return status
    }
}