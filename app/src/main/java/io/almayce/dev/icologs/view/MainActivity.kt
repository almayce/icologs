package io.almayce.dev.icologs.view

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import io.almayce.dev.icologs.R
import io.almayce.dev.icologs.databinding.ActivityMainBinding
import io.almayce.dev.icologs.global.ICOcollection
import io.almayce.dev.icologs.global.PrefHelper
import io.almayce.dev.icologs.global.SchedulersTransformer
import io.almayce.dev.icologs.model.firebase.DatabaseReader
import io.almayce.dev.icologs.presenter.MainPresenter
import io.almayce.dev.icologs.view.adapter.SectionsPagerAdapter

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
        if (!ICOcollection.getPinICO(PrefHelper.set).isEmpty())
            bn.btBack.visibility = View.VISIBLE
        bn.btBack.setOnClickListener({
            bn.container.setCurrentItem(bn.container.currentItem - 1)
        })
        bn.btForward.setOnClickListener({
            bn.container.setCurrentItem(bn.container.currentItem + 1)
        })
        DatabaseReader.onReaded
                .compose(SchedulersTransformer())
                .subscribe({
                    onPageSelected(1)
                })
        PrefHelper.onPinned
                .compose(SchedulersTransformer())
                .subscribe({
                    checkPinList()
                })
        pr.checkRate()
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
                if (ICOcollection.getPinICO(PrefHelper.set).isEmpty()) {
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
        if (ICOcollection.getPinICO(PrefHelper.set).isEmpty()) {
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
                .setNegativeButton(resources.getString(R.string.dialog_neg_btn)) {
                    dialog, which ->
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
