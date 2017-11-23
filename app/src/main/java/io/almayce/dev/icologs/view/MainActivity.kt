package io.almayce.dev.icologs.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.databinding.ActivityMainBinding
import io.almayce.dev.icologs.model.ICOcollection
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.DatabaseReader
import io.almayce.dev.icologs.presenter.MainPresenter
import io.almayce.dev.icologs.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_connection.*


class MainActivity : MvpAppCompatActivity(), MainView, ViewPager.OnPageChangeListener {

    @InjectPresenter
    lateinit var pr: MainPresenter
    private lateinit var bn: ActivityMainBinding
    private lateinit var pagerAdapter: SectionsPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PrefHelper.initPref(this)
        bn = DataBindingUtil.setContentView(this, R.layout.activity_main)
        pagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        bn.container.adapter = pagerAdapter
        bn.container.addOnPageChangeListener(this)
        bn.container.currentItem = 1
        bn.container.offscreenPageLimit = 3
        if (!ICOcollection.pin!!.isEmpty())
            bn.btBack.visibility = View.VISIBLE
        bn.btBack.setOnClickListener({
            bn.container.setCurrentItem(bn.container.currentItem - 1)
        })
        bn.btForward.setOnClickListener({
            bn.container.setCurrentItem(bn.container.currentItem + 1)
        })
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        bn.btSearch.setOnClickListener({

            if (bn.rlSearch.visibility == View.INVISIBLE) {
                bn.rlSearch.visibility = View.VISIBLE
                bn.actvSearch.requestFocus()
                imm.showSoftInput(bn.actvSearch, InputMethodManager.SHOW_IMPLICIT)
            } else {
                bn.rlSearch.visibility = View.INVISIBLE
                bn.actvSearch.clearFocus()
                bn.actvSearch.setText("")
                imm.hideSoftInputFromWindow(bn.actvSearch.getWindowToken(), 0)
            }
        })
        bn.ivSearchClose.setOnClickListener({
            bn.rlSearch.visibility = View.INVISIBLE
            bn.actvSearch.clearFocus()
            bn.actvSearch.setText("")
            imm.hideSoftInputFromWindow(bn.actvSearch.getWindowToken(), 0)
        })
        bn.actvSearch.setOnItemClickListener { adapterView, view, i, l ->
            onSearchItemClick(view)
            bn.actvSearch.clearFocus()
            bn.actvSearch.setText("")
        }
        btRefresh.setOnClickListener({
            checkConnection()
        })
        ICOcollection.onSorted
                .compose(SchedulersTransformer())
                .subscribe({
                    onPageSelected(1)
                    bn.actvSearch.setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pr.getStringList()))
                    bn.rlLoading.visibility = View.INVISIBLE
                })
        PrefHelper.onPinned
                .compose(SchedulersTransformer())
                .subscribe({
                    checkPinList()
                })
        pr.checkRate()

        checkConnection()
    }

    private fun checkConnection() {
        if (isOnline()) {
            bn.rlConnection.visibility = View.INVISIBLE
            bn.rlLoading.visibility = View.VISIBLE
            pr.read()
        } else
            bn.rlConnection.visibility = View.VISIBLE
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun onSearchItemClick(view: View) {
        val tv = view as TextView
        val index = pr.getStringList().indexOf(tv.text.toString())
        val target = pr.getList().get(index)
        val intent = Intent(this, ItemActivity::class.java)

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        with(intent) {
            putExtra("id", target.id)
            putExtra("title", target.title)
            putExtra("descript", target.descript)
            putExtra("status", target.status)
            putExtra("startDate", target.startDate)
            putExtra("endDate", target.endDate)
            putExtra("symbol", target.symbol)
            putExtra("tokenPrice", target.tokenPrice)
            putExtra("sum", target.sum)
            putExtra("invest", target.invest)
            putExtra("link", target.link)
            putExtra("whitePaperLink", target.whitePaperLink)
            putExtra("news", target.news)
            putExtra("preSaleDate", target.preSaleDate)
            putExtra("newDate", target.newDate)
            putExtra("fix", target.fix)
            putExtra("youTubeUrl", target.youTubeUrl)
        }

        startActivity(intent)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {

        when {
            position == 0 -> {
                bn.btBack.visibility = View.INVISIBLE
                bn.btForward.visibility = View.VISIBLE
                if (ICOcollection.pin!!.isEmpty()) {
                    bn.container.currentItem = 1
                }
            }
            position == 1 -> {
                bn.btBack.visibility = View.VISIBLE
                bn.btForward.visibility = View.VISIBLE
                checkPinList()

            }
            position == 2 -> {
                bn.btBack.visibility = View.VISIBLE
                bn.btForward.visibility = View.VISIBLE
            }
            position == 3 -> {
                bn.btBack.visibility = View.VISIBLE
                bn.btForward.visibility = View.INVISIBLE
            }
        }
    }

    fun checkPinList() {
        if (ICOcollection.pin.isEmpty()) {
            bn.container.currentItem = 1
            bn.btBack.visibility = View.INVISIBLE
            bn.btForward.visibility = View.VISIBLE
        }
    }

    override fun showRateDialog() {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        else
            builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(resources.getString(R.string.dialog_title))
                .setMessage(resources.getString(R.string.dialog_message))
                .setPositiveButton(resources.getString(R.string.dialog_pos_btn)) { dialog, which ->
                    pr.confirmRate()
                    goToGooglePlay()
                }
                .setNegativeButton(resources.getString(R.string.dialog_neg_btn)) { dialog, which ->
                    dialog.cancel()
                }
                .setIcon(R.drawable.ic_star_border_black_24dp)
                .show()
    }

    fun goToGooglePlay() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=io.almayce.dev.icologs"))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        startActivity(browserIntent)
    }
}
