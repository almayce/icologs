package io.almayce.dev.icologs.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import io.almayce.dev.icologs.view.fragment.ActiveFragment
import io.almayce.dev.icologs.view.fragment.LikeFragment
import io.almayce.dev.icologs.view.fragment.RecentFragment
import io.almayce.dev.icologs.view.fragment.UpcomingFragment


/**
 * Created by almayce on 28.08.17.
 */
class SectionsPagerAdapter(var context: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when {
            position == 0 -> return LikeFragment()
            position == 1 -> return ActiveFragment()
            position == 2 -> return UpcomingFragment()
            position == 3 -> return RecentFragment()
        }
        return ActiveFragment()
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 4
    }
}