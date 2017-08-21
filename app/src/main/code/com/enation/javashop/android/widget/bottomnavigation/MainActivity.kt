package com.enation.javashop.android.widget.bottomnavigation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import com.enation.javashop.android.widget.navigationview.NavigationIconModel
import com.enation.javashop.android.widget.navigationview.NavigationModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var data = ArrayList<NavigationModel>()
        data.add(NavigationModel("首页",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add( NavigationModel("分类",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add(NavigationModel("购物车",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add(NavigationModel("我的",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add( NavigationModel("分类",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add(NavigationModel("购物车",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add(NavigationModel("我的",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        data.add( NavigationModel("分类",R.mipmap.ic_launcher,R.mipmap.ic_launcher_round))
        navigation.setNomalColor(R.color.abc_color_highlight_material)
        navigation.setSelectColor(R.color.colorPrimary)
        navigation.setData(data,{
            i ->
            ViewPager(baseContext).setCurrentItem(i,false)
        })

    }
}
