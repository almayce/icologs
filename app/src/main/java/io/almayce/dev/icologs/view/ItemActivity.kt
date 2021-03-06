package io.almayce.dev.icologs.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.databinding.ActivityItemBinding
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.presenter.ItemPresenter
import io.almayce.dev.icologs.adapter.GridViewAdapter
import kotlinx.android.synthetic.main.activity_item.*
import java.util.concurrent.TimeUnit

/**
 * Created by almayce on 29.08.17.
 */
class ItemActivity : MvpAppCompatActivity(), ItemView {


    @InjectPresenter
    lateinit var pr: ItemPresenter
    private lateinit var bn: ActivityItemBinding
    private var news = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pr.init()
        bn = DataBindingUtil.setContentView(this, R.layout.activity_item)
        bn.btBack.setOnClickListener({ onBackPressed() })
        bn.btShare.setOnClickListener({ share() })
        bn.tvTitle.text = intent.getStringExtra("title")
        bn.tvDescript.text = intent.getStringExtra("descript")
        checkPin()
        checkStatus()
        pr.setDate(intent.getStringExtra("status"),
                intent.getStringExtra("startDate"),
                intent.getStringExtra("endDate"))
        bn.tvSymbol.text = intent.getStringExtra("symbol")
        bn.tvTokenPrice.text = intent.getStringExtra("tokenPrice")
        bn.tvSum.text = intent.getStringExtra("sum")
        bn.tvInvest.text = intent.getStringExtra("invest")
        news.addAll(intent.getStringArrayListExtra("news"))
        bn.gvNews.adapter = GridViewAdapter(this, news)
        bn.gvNews.setOnItemClickListener { adapterView, view, i, l -> goTo(view.contentDescription.toString()) }
        bn.rlPin.setOnClickListener({ updatePin() })
        bn.rlLink.setOnClickListener({ goTo(intent.getStringExtra("link")) })
        bn.rlWhitePaper.setOnClickListener({ goTo(intent.getStringExtra("whitePaperLink")) })
        bn.tvSupport.setOnClickListener({ startEmailIntent() })
        bn.rlPreSale.visibility = if (isPreSale(intent.getStringExtra("preSaleDate"))) View.VISIBLE else View.INVISIBLE
        bn.rlPreSale.layoutParams.width = if (isPreSale(intent.getStringExtra("preSaleDate"))) ViewGroup.LayoutParams.WRAP_CONTENT else 0
        bn.rlNew.visibility = if (isNew(intent.getStringExtra("newDate"))) View.VISIBLE else View.INVISIBLE
        bn.rlNew.layoutParams.width = if (isNew(intent.getStringExtra("newDate"))) ViewGroup.LayoutParams.WRAP_CONTENT else 0

        val youTubeUrl: String? = intent.getStringExtra("youTubeUrl")
        println("!!!" + youTubeUrl)
        if (youTubeUrl != null)
            if (youTubeUrl.isNotEmpty())
                try {
                    initPlayer(youTubeUrl.split("v=")[1])
                } catch (e: IndexOutOfBoundsException) {
                    playerHide()
                }
            else playerHide()
        else playerHide()
    }

    private fun playerHide() {
        val layoutParamsHide = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        youTubePlayerSupportFragment.layoutParams = layoutParamsHide
        youTubePlayerSupportFragment.visibility = View.INVISIBLE
        vSeparatorPlayer2.visibility = View.INVISIBLE
    }

    fun initPlayer(url: String) {
        val playerFragment = YouTubePlayerSupportFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.youTubePlayerSupportFragment, playerFragment)
                .commit()

        playerFragment.initialize(getString(R.string.api_key), object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(arg0: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                youTubePlayer.cueVideo(url)

            }

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {
                playerHide()
            }
        })
    }

    fun isPreSale(millis: String): Boolean =
            millis.toLong() > 0
//                    && System.currentTimeMillis() < millis.toLong()

    fun isNew(millis: String): Boolean =
            millis.toLong() > 0 && TimeUnit.HOURS.convert(System.currentTimeMillis() - millis.toLong(), TimeUnit.MILLISECONDS).toInt() < 72

    fun goTo(link: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        if (!link.isNullOrEmpty())
            try {
                startActivity(browserIntent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Неверная ссылка", Toast.LENGTH_SHORT).show()
            }
    }

    fun startEmailIntent() {
        val to = "support@icologs.com"
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        email.putExtra(Intent.EXTRA_SUBJECT, "")
        email.putExtra(Intent.EXTRA_TEXT, "")

        //для того чтобы запросить email клиент устанавливаем тип
        email.type = "message/rfc822"

        startActivity(Intent.createChooser(email, "Send email to $to via:"))
    }

    fun goToSupportActivity() {
        var intent = Intent(this, SupportActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun updatePin() {
        PrefHelper.updatePin(intent.getStringExtra("id"))
        bn.ivPin.isEnabled = false
    }

    fun checkPin() {
        var target = intent.getStringExtra("id")
        if (PrefHelper.set.contains(target))
            pin()
        else
            unpin()
    }

    override fun pin() {
        bn.ivPin.setImageResource(R.drawable.ic_favorite_red_24dp)
        bn.tvPin.text = "like"
        bn.tvPinDescript.text = resources.getString(R.string.like_project)
        bn.ivPin.isEnabled = true
    }

    override fun unpin() {
        bn.ivPin.setImageResource(R.drawable.ic_favorite_gray_24dp)
        bn.tvPin.text = "like"
        bn.tvPinDescript.text = resources.getString(R.string.not_like_project)
        bn.ivPin.isEnabled = true

    }

    fun checkStatus() {
        var target = intent.getStringExtra("status")
        when {
            target == "up" -> {
                bn.tvStatus.text = resources.getString(R.string.item_upcoming_ico)
                bn.tvStatus.setTextColor(ContextCompat.getColor(applicationContext,
                        R.color.status_up))
            }
            target == "now"
            -> {
                bn.tvStatus.text = resources.getString(R.string.item_active_ico)
                bn.tvStatus.setTextColor(ContextCompat.getColor(applicationContext,
                        R.color.status_now))
            }
            target == "end"
            -> {
                bn.rlStatus.visibility = View.INVISIBLE
                bn.rlEnd.visibility = View.VISIBLE
            }
        }
    }

    override fun setTimer(array: Array<String>) {
        array[0] = convertDays(array[0])
        bn.tvD.text = array[0]
        bn.tvH.text = array[1]
        bn.tvM.text = array[2]
        bn.tvS.text = array[3]
    }

    fun convertDays(days: String): String {
        var target = "00"
        if (days.toInt() > 0)
            target = (days.toInt() - 1).toString()
        if (target.length < 2)
            return "0$target"
        return target
    }

    override fun setDate(startDate: String, endDate: String, startTime: String, endTime: String) {
        bn.tvStartDate.text = "$startDate | $startTime"
        bn.tvEndDate.text = "$endDate | $endTime"
    }

    private fun share() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, bn.tvTitle.text)
        sharingIntent.putExtra(Intent.EXTRA_TEXT,
                "${bn.tvTitle.text}\n" +
                        "${bn.tvDescript.text}\n\n" +
                        "${intent.getStringExtra("link")}\n" +
                        "Из приложения app.icologs.com.")
        startActivity(Intent.createChooser(sharingIntent, "Send via:"))
    }
}