package cn.kirilenkoo.www.coolpets.ui.view

import android.content.Context
import android.graphics.Path
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.model.Tag
import cn.kirilenkoo.www.coolpets.thirdparty.GlideApp
import cn.kirilenkoo.www.coolpets.util.convertDp2Px
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.math.exp
import kotlin.math.pow

class CoolPetHeaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var mTags: List<Tag>
    private lateinit var mViewPager: ViewPager
    private var mTagViews = arrayOfNulls<ImageView>(3)
    private val mOnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(dy>0){//scroll down
                collapse(dy)
            }else{// scroll up
                expand(dy)
            }
        }
    }
    private lateinit var mBottomViewWrapper: WeakReference<View>
    init {

    }


    fun setupPagerAction(tags: List<Tag>, viewPager: ViewPager){
        setTags(tags)
        setViewPager(viewPager)
    }
    fun setTags(tags: List<Tag>){
        mTags = tags
        val paddingHorizontal = convertDp2Px(20,context)
        val paddingVertical = convertDp2Px(12,context)
        val height = layoutParams.height
        initLWidth = (height/3).toFloat()
        initSWidth = (height/4).toFloat()
        for(i in 0..2){
            val flp = when (i){
                0 -> FrameLayout.LayoutParams(height/4,height/4).also {
//                    it.setMargins(paddingHorizontal,0,0,paddingVertical)
                    it.gravity = Gravity.BOTTOM
                }
                1 -> FrameLayout.LayoutParams(height/3, height/3).also {
//                    it.setMargins(0, paddingVertical,0,0)
                    it.gravity = Gravity.CENTER or Gravity.TOP
                }
                else -> FrameLayout.LayoutParams( height/4, height/4).also {
//                    it.setMargins(0,0,paddingHorizontal,paddingVertical)
                    it.gravity = Gravity.RIGHT or Gravity.BOTTOM
                }
            }
            val tagView = ImageView(context)
            GlideApp.with(context).load(mTags[i].url).into(tagView)
            addView(tagView, flp)
            mTagViews[i] = tagView
        }
    }
    private var initTopY: Float? = null
    private var initBottomY: Float? = null
    private var initLeftX: Float? = null
    private var initMiddleX: Float? = null
    private var initRightX: Float? = null
    private var initA: Float? = null
    private var initLWidth: Float? = null
    private var initSWidth: Float? = null
    private var initXhalfLength: Float? = null
    private val minHeight = convertDp2Px(50,context)
    private var maxHeight : Int? = null


    fun setViewPager(viewPager: ViewPager){
        mViewPager = viewPager
        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

//                Timber.d("position: $position | positionOffset: $positionOffset | offsetPixels: $positionOffsetPixels")
                val centerxypair = generateCenterXY(position, positionOffset)
                mTagViews[1]?.layoutParams = mTagViews[1]?.layoutParams.also {
                    it!!.width = centerxypair[2].toInt()
                    it!!.height = centerxypair[2].toInt()
                }
                mTagViews[1]?.x = centerxypair[0]-mTagViews[1]?.width!!/2
                mTagViews[1]?.y = centerxypair[1]-mTagViews[1]?.width!!/2

            }

            override fun onPageSelected(position: Int) {
            }

        })
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (initTopY == null){
            initTopY = mTagViews[1]?.y
            initLeftX = mTagViews[0]?.x
            initMiddleX = mTagViews[1]?.x
            initRightX = mTagViews[2]?.x
            initXhalfLength = initMiddleX!!+initLWidth!!/2-initLeftX!!-initSWidth!!/2
            maxHeight = layoutParams.height
        }
        initBottomY = mTagViews[0]?.y
        initA = (initBottomY!!+initSWidth!!/2-initTopY!!-initLWidth!!/2)/initXhalfLength!!.pow(2)
    }


    private fun generateCenterXY(position: Int, positionOffset: Float): Array<Float> {
        //position = 0, offset = 0 , x = middlex, y = topy: position = 0, offset = 1, x = rightx, y = bottomy
        //position = 1, offset = 0 , x = leftx, y = bottomy: position = 1, offset = 1, x = middlex, y = topy
        val xCenter = when (position) {
            0 -> initMiddleX!!+initLWidth!!/2 + initXhalfLength!! * (1f-positionOffset)
            1 -> initMiddleX!!+initLWidth!!/2 - initXhalfLength!! * positionOffset
            else -> initMiddleX!!+initLWidth!!/2 - initXhalfLength!!
        }
        val yCenter = initA!!*(initMiddleX!!+initLWidth!!/2 - xCenter).pow(2)+initLWidth!!/2
        val width = when (position) {
            0 -> initLWidth!! - (1f-positionOffset)*(initLWidth!!-initSWidth!!)
            1 -> initLWidth!! - (positionOffset)*(initLWidth!!-initSWidth!!)
            else -> initSWidth!!
        }
        return arrayOf(xCenter,yCenter, width)
    }

    fun addBottomView(view: View){
        mBottomViewWrapper = WeakReference(view)
    }

    fun addScrollChildFragment(fragment: Fragment){
        val recyclerView: RecyclerView? = fragment.view?.findViewById(R.id.recyclerView)
        recyclerView?.let {
            it.removeOnScrollListener(mOnScrollListener)
            it.addOnScrollListener(mOnScrollListener)
        }
    }

    fun collapse(dy: Int){
        Timber.d("collapsing : $dy / $height ")
        this.layoutParams = this.layoutParams.also {
            if(it.height-dy > minHeight) it.height = it.height-dy
        }
    }
    fun expand(dy: Int){
        Timber.d( " expanding : $dy / $height")
        this.layoutParams = this.layoutParams.also {
            if(it.height-dy < maxHeight!!) it.height = it.height-dy
        }
    }
}