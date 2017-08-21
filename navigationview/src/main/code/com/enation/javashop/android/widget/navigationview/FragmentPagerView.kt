package com.enation.javashop.android.widget.navigationview

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Fragment容器PagerView
 */
class FragmentPagerView(context: Context?) : ViewPager(context) {

    /**
     * 设置Fragment
     * @param fragments fragment集合
     * @param fragementManager fragment控制器
     */
    fun setFragments(fragments: ArrayList<Fragment>, fragementManager: FragmentManager) {
        adapter = object : FragmentPagerAdapter(fragementManager){
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {

            }

            override fun destroyItem(container: View?, position: Int, `object`: Any?) {

            }
        }
            offscreenPageLimit = 1
    }

    /**
     * 重写拦截触摸事件 防止多点触摸崩溃
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val bool: Boolean
        try {
            bool = super.onInterceptTouchEvent(ev)
            return bool
        } catch (localIllegalArgumentException: IllegalArgumentException) {
            localIllegalArgumentException.printStackTrace()
        }

        return false
    }

    /**
     * 重写拦截触摸时间 防止多点触摸崩溃
     */
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        val bool: Boolean
        try {
            bool = super.onTouchEvent(ev)
            return bool
        } catch (localIllegalArgumentException: IllegalArgumentException) {
            localIllegalArgumentException.printStackTrace()
        }

        return false
    }

}