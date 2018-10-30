package com.enation.javashop.android.widget.navigationview

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.WindowManager

/**
 * 底部导航视图 继承LinearLayout，实现导航视图协议
 * 作用：提供导航作用
 * @author LDD
 */
class NavigationView : LinearLayout,NavigationViewAgreement {


    /**
     * 导航Item的个数
     */
    private  var itemNum   = 0

    /**
     * 导航Item的高
     */
    private  var itemContentHeight = 0

    /**
     * 导航Item的宽
     */
    private  var itemContentWidth = 0

    /**
     * 导航视图的所有Item
     */
    private  var subViews :ArrayList<LinearLayout>? =null

    /**
     * 父容器高度
     */
    private  var parentHeight  = 0

    /**
     * 父容器宽度
     */
    private  var parentWeight  = 0

    /**
     * 选定状态时 字体颜色
     */
    private  var selectColor   = R.color.selectColor

    /**
     * 未选定状态时 字体颜色
     */
    private  var nomalColor    = R.color.nomalColor

    /**
     * 图片控件tag
     */
    private  var IAMGE_VIEW_TAG = "NavigationImageView"

    /**
     * 文字控件tag
     */
    private  var TEXT_VIEW_TAG = "NavigationTextView"

    /**
     * 只有图标的数据源
     */
    private  var dataIcon : Array<NavigationIconModel>? = null

    /**
     * 图标文字的数据源
     */
    private var  data :ArrayList<NavigationModel>? = null

    private val interceptors = ArrayList<TabActionInterceptor>()


    fun addInterceptor(interceptor: TabActionInterceptor){
        interceptors.add(interceptor)
    }

    fun interceptorClear(){
        interceptors.clear()
    }

    /**
     * 构造方法
     * @param context 上下文
     * @param attrs   xml参数
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        /**初始化*/
        init(attrs)
    }

    /**
     * 初始化参数以及宽高
     * @param attrs   xml参数
     */
    private fun init(attrs: AttributeSet?){
        /**视图横向*/
        this.orientation = HORIZONTAL
        /**字视图居中*/
        this.gravity = Gravity.CENTER
        /**获取宽高参数*/
        for (i in 0..attrs!!.attributeCount - 1) {
            /**获取高 */
            if (attrs.getAttributeName(i).equals("layout_height")) {
                parentHeight = Integer.valueOf(strFilter(attrs.getAttributeValue(i)))
            }
            /**获取宽 */
            if (attrs.getAttributeName(i).equals("layout_width")) {
                parentWeight = Integer.valueOf(strFilter(attrs.getAttributeValue(i)))
            }
        }
        /**如果parentHeight为自适应或者全屏，直接设置为屏幕高*/
        if (parentHeight==-1||parentHeight==-2){
            parentHeight = getScreenHeight()
        }
        /**如果parentWeight为自适应或者全屏，直接设置为屏幕宽*/
        if (parentWeight==-1||parentWeight==-2){
            parentWeight = getScreenWidth()
        }
        /**初始化item高度*/
        initItemHeight()
    }

    /**
     * 从字符串中获取数字，并且去除小数点
     * @param str    StringValue
     * @return       去除完毕的字符串
     */
    private fun strFilter(str: String): String {
        var value = str.replace("[a-zA-Z]".toRegex(), "")
        if (value.contains(".")) {
            value = value.substring(0, value.indexOf("."))
        }
        return value
    }

    /**
     * 设置数据源
     */
    override fun setData(data: ArrayList<NavigationModel>, event: (Int) -> Unit) {
            setData(data,0,event)
    }

    /**
     * 设置数据源
     */
    override fun setData(data: Array<NavigationIconModel>, event: (Int) -> Unit) {
            setData(data,0,event)
    }

    /**
     * 显示 带title
     * @param data         数据
     * @param selectIndex  哪一个Item处于选定状态
     * @param event        监听事件
     */
    override fun setData(data: ArrayList<NavigationModel>, selectIndex: Int, event:(Int) ->Unit) {
        itemNum += data.size
        this.data = data
        initItemWidth()
        if (subViews==null){
            subViews = ArrayList()
        }
        for (datum in data) {
            var item = createItem(false,datum.nomalImage,datum.itemTitle)
            item.setOnClickListener {

                if (interceptors.size > 0){
                    for (interceptor in interceptors) {
                      val isInterceptor = interceptor.interceptor(subViews!!.indexOf(item))
                        if (isInterceptor){
                            return@setOnClickListener
                        }
                    }
                }

                subViews!!.forEach { linear ->
                    if (subViews!!.indexOf(item)==subViews!!.indexOf(linear)){
                        item.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(datum.selectImage)
                        item.findViewWithTag<TextView>(TEXT_VIEW_TAG).setTextColor(getColor(selectColor))
                    }else{
                        linear.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(data[subViews!!.indexOf(linear)].nomalImage)
                        linear.findViewWithTag<TextView>(TEXT_VIEW_TAG).setTextColor(getColor(nomalColor))
                    }
                }
                event.invoke(subViews!!.indexOf(item))
            }
            addView(item)
            subViews!!.add(item)
            if (subViews!!.size-1==selectIndex){
                item.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(datum.selectImage)
                item.findViewWithTag<TextView>(TEXT_VIEW_TAG).setTextColor(getColor(selectColor))
            }
        }
    }

    /**
     * 显示 仅图标
     * @param data 数据源
     * @param selectIndex 第几个选中
     * @param event    监听事件
     */
    override fun setData(data: Array<NavigationIconModel>,selectIndex: Int,event:(Int) ->Unit) {
        itemNum = data.size
        dataIcon = data
        initItemWidth()
        if (subViews==null){
            subViews = ArrayList()
        }
        for (datum in data) {
            var item = createItem(true,datum.nomalImage,null)
            item.setOnClickListener {
                subViews!!.forEach { linear ->
                    if (subViews!!.indexOf(item)==subViews!!.indexOf(linear)){
                        item.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(datum.selectImage)
                    }else{
                        linear.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(data[subViews!!.indexOf(linear)].nomalImage)
                    }
                }
                event.invoke(subViews!!.indexOf(item))
            }
            addView(item)
            subViews!!.add(item)
            if (subViews!!.size-1==selectIndex){
                item.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(datum.selectImage)
            }
        }
    }

    /**
     * 绘制item视图
     * @param isOnlyIcon 是否只绘制图片
     * @param icon 图片ID
     * @param text 文字
     * @return  Item视图
     */
    private fun createItem (isOnlyIcon :Boolean,icon :Int,text :String?):LinearLayout{
        var item = LinearLayout(context)
        item.gravity = Gravity.CENTER
        val itemParems = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,1.0f)
        item.layoutParams = itemParems
        if (isOnlyIcon){
            var iconView = ImageView(context)
            var iconwidth = itemContentHeight
            if (itemContentHeight> itemContentWidth){
                iconwidth  = itemContentWidth
            }
            iconView.tag = "NavigationImageView"
            iconView.layoutParams = LinearLayout.LayoutParams(iconwidth,iconwidth)
            iconView.scaleType = ImageView.ScaleType.FIT_CENTER
            iconView.setImageResource(icon)
            item.addView(iconView)
        }else{
            item.orientation = VERTICAL
            var iconView = ImageView(context)
            iconView.tag = "NavigationImageView"
            var iconwidth  =  (itemContentHeight*0.7).toInt()
            var textheight = (itemContentHeight*0.3).toInt()
            if (iconwidth> itemContentWidth){
                    iconwidth   = (itemContentWidth *0.7).toInt()
                    textheight  = (itemContentWidth *0.3).toInt()
            }
            iconView.layoutParams = LinearLayout.LayoutParams(iconwidth,iconwidth)
            iconView.scaleType = ImageView.ScaleType.FIT_CENTER
            iconView.setImageResource(icon)
            item.addView(iconView)
            var textview = TextView(context)
            textview.gravity = Gravity.CENTER
            textview.setLines(1)
            textview.setTag("NavigationTextView")
            textview.height = textheight
            textview.textSize = (px2sp(context,textheight)*0.7).toFloat()
            textview.text = text
            textview.setTextColor(getColor(nomalColor))
            item.addView(textview)
        }
        return item
    }

    /**
     * 设置选中Item的字体颜色
     * @param color 颜色
     */
    fun setSelectColor(color:Int){
        selectColor = color
    }

    /**
     * 设置未选中Item的字体颜色
     * @param color 颜色
     */
    fun setNomalColor(color:Int){
        nomalColor = color
    }


    /**
     * 设置绑定ViewPager
     * @param viewpager 控件
     */
    fun withViewPager(viewpager:ViewPager?){
        if (viewpager==null){
            return
        }
        viewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position>subViews!!.size-1){
                    return
                }
                for (subView in subViews!!) {
                    if (subViews!!.indexOf(subView)==position){
                        if (subView.findViewWithTag<TextView>(TEXT_VIEW_TAG)==null){
                            subView.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(dataIcon!![subViews!!.indexOf(subView)].selectImage)
                        }else{
                            subView.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(data!![subViews!!.indexOf(subView)].selectImage)
                            subView.findViewWithTag<TextView>(TEXT_VIEW_TAG).setTextColor(getColor(selectColor))
                        }
                    }else{
                        if (subView.findViewWithTag<TextView>(TEXT_VIEW_TAG)==null){
                            subView.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(dataIcon!![subViews!!.indexOf(subView)].nomalImage)
                        }else{
                            subView.findViewWithTag<ImageView>(IAMGE_VIEW_TAG).setImageResource(data!![subViews!!.indexOf(subView)].nomalImage)
                            subView.findViewWithTag<TextView>(TEXT_VIEW_TAG).setTextColor(getColor(nomalColor))
                        }
                    }
                }
            }

        })
    }

    /**
     * 初始化Item的高
     */
    private fun initItemHeight() {
        itemContentHeight = dip2px(context,(parentHeight*0.85).toFloat())
    }

    /**
     * 初始化Item的宽
     */
    private fun initItemWidth(){
        itemContentWidth = ((parentWeight/itemNum*0.8).toInt())
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue   px值
     * *
     * @param context   上下文
     * *
     * @return          sp值
     */
    private fun px2sp(context: Context, pxValue: Int): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 根据手机分辨率从DP转成PX
     * @param context 上下文
     * *
     * @param dpValue dp值
     * *
     * @return        px值
     */
    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度
     */
    private fun getScreenWidth():Int{
        var wm =context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return  wm.defaultDisplay.width
    }

    /**
     * 获取屏幕高度
     */
    private fun getScreenHeight(): Int{
        var wm =context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return   wm.defaultDisplay.height
    }

    private fun getColor(color :Int) :Int{
        var result:Int = 0
        try {
          result =  ContextCompat.getColor(context,color)
        }catch (e :Exception){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    result = resources.getColor(color,null)
                }
            return result
        }
        return result
    }
}