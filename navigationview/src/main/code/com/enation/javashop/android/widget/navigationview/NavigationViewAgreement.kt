package com.enation.javashop.android.widget.navigationview

/**
 * 导航视图接口
 * @author LDD
 */
interface NavigationViewAgreement {

    fun setData(data :ArrayList<NavigationModel>,selectIndex:Int,event:(Int) ->Unit)

    fun setData(data :Array<NavigationIconModel>,selectIndex:Int,event:(Int) ->Unit)

    fun setData(data :ArrayList<NavigationModel>,event:(Int) ->Unit)

    fun setData(data :Array<NavigationIconModel>,event:(Int) ->Unit)
}