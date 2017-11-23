package io.almayce.dev.icologs.model

import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
    }

    fun read() {
        var target = ICO()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(o: DataSnapshot) {
                ICOcollection.instance.clear()
                for (oo: DataSnapshot in o.children) {
                    for (ooo: DataSnapshot in oo.children) {
                        val key = ooo.key
                        val s = String::class.java


                        when (key){
                            "title" -> target.title = ooo.getValue(s)!!
                            "descript" -> target.descript = ooo.getValue(s)!!
                            "startDate" -> target.startDate = ooo.getValue(s)!!
                            "endDate" -> target.endDate = ooo.getValue(s)!!
                            "symbol" -> target.symbol = ooo.getValue(s)!!
                            "tokenPrice" -> target.tokenPrice = ooo.getValue(s)!!
                            "sum" -> target.sum = ooo.getValue(s)!!
                            "invest" -> target.invest = ooo.getValue(s)!!
                            "link" -> target.link = ooo.getValue(s)!!
                            "whitePaperLink" -> target.whitePaperLink = ooo.getValue(s)!!
                            "news" -> target.news = getNewsArrayList(ooo.children)
                            "zpreSaleDate" -> target.preSaleDate = ooo.getValue(s)!!
                            "znewDate" -> target.newDate = ooo.getValue(s)!!
                            "zfix" -> target.fix = ooo.getValue(s)!!
                            "zYouTubeUrl" -> target.youTubeUrl = ooo.getValue(s)!!
                        }
                    }

                    val lang = Locale.getDefault().getDisplayLanguage()
                    val titleArray = target.title.split("<split>")
                    val descriptArray = target.descript.split("<split>")

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
                }
                onReaded.onNext(true)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun getNewsArrayList(snapshot: MutableIterable<DataSnapshot>): ArrayList<String?> {
        val target = ArrayList<String?>()
        for (o: DataSnapshot in snapshot)
            target.add(o.getValue(String::class.java))
        return target
    }

    fun setStatus(startDate: String, endDate: String): String {
        val s = startDate.toLong()
        val e = endDate.toLong()
        var c = System.currentTimeMillis()

//        val utc = TimeZone.getTimeZone("UTC")
//        val gmt = TimeZone.getTimeZone("GMT")
//
//        val sOffSet = utc.getOffset(s)
//        val eOffSet = utc.getOffset(e)
//
//        val calStart = Calendar.getInstance(gmt)
//        calStart.timeInMillis = s
//        calStart.add(Calendar.MILLISECOND, sOffSet)
//
//        val calEnd = Calendar.getInstance(gmt)
//        calEnd.timeInMillis = e
//        calEnd.add(Calendar.MILLISECOND, eOffSet)
//
//        val calCurrent = Calendar.getInstance()
//        calCurrent.add(Calendar.MILLISECOND,
//                calCurrent.timeZone.getOffset(calCurrent.timeInMillis))
//
//        val start = calStart.timeInMillis
//        val end = calEnd.timeInMillis
//        val current = System.currentTimeMillis()

        var status = "now"

        if (s > c)
            status = "up"
        else if (s < c && e > c)
            status = "now"
        else if (e < c)
            status = "end"

        return status
    }
}