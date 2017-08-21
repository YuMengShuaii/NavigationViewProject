package com.enation.javashop.android.widget.navigationview

/**
 * 导航视图数据Model 显示图片文字
 * @author LDD
 */
 data class NavigationModel(/**item文字*/
                            var itemTitle :String,
                            /**选中图片*/
                            var selectImage :Int,
                            /**未选中图片*/
                            var nomalImage:Int)
