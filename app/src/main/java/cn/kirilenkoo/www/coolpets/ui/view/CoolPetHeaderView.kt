package cn.kirilenkoo.www.coolpets.ui.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.model.Tag
import cn.kirilenkoo.www.coolpets.thirdparty.GlideApp
import cn.kirilenkoo.www.coolpets.util.convertDp2Px
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.math.pow

class CoolPetHeaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var mTags: List<Tag>
    private lateinit var mViewPager: ViewPager
    private var mTagViews = arrayOfNulls<ImageView>(3)
    private val mExpandSensitive  = 3
    private val mOnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(isAnimating) return
            if(dy>mExpandSensitive){//scroll up
                collapse(dy)
            }else if(dy<-mExpandSensitive){// scroll down
                expand(dy)
            }
        }
    }
    private lateinit var mBottomViewWrapper: WeakReference<View>
    private var mTextView: TextView = TextView(context)

    private var initTopY: Float? = null
    private var initBottomY: Float? = null
    private var initLeftX: Float? = null
    private var initMiddleX: Float? = null
    private var initRightX: Float? = null
    private var initA: Float? = null
    private var initLWidth: Float? = null
    private var initSWidth: Float? = null
    private var initXhalfLength: Float? = null
    private val minHeight = convertDp2Px(40,context)
    private var maxHeight : Int? = null
    private lateinit var collapseAnimator: ValueAnimator
    private lateinit var expandAnimator: ValueAnimator
    private val animDuration = context.resources.getInteger(R.integer.anim_duration).toLong()

    init {
        mTextView.setTextColor(Color.WHITE)
        val flp = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER or Gravity.BOTTOM }
        addView(mTextView,flp)
    }

    fun setupPagerAction(tags: List<Tag>, viewPager: ViewPager, bottomView: View){
        setTags(tags)
        setViewPager(viewPager)
        setBottomView(bottomView)
    }

    fun setTags(tags: List<Tag>){
        val tabStatics = arrayOf(R.drawable.icon_tab_butterfly, R.drawable.icon_tab_turtle, R.drawable.icon_tab_frog)
        mTags = tags
        val paddingHorizontal = convertDp2Px(20,context)
        val paddingVertical = convertDp2Px(12,context)
        val height = layoutParams.height
        val largeInt = height/2
        val smallInt = height/4
        initLWidth = largeInt.toFloat()
        initSWidth = smallInt.toFloat()
        for(i in 0..2){
            val flp = when (i){
                0 -> FrameLayout.LayoutParams(smallInt,smallInt).also {
//                    it.setMargins(paddingHorizontal,0,0,paddingVertical)
                    it.gravity = Gravity.BOTTOM
                }
                1 -> FrameLayout.LayoutParams(largeInt, largeInt).also {
//                    it.setMargins(0, paddingVertical,0,0)
                    it.gravity = Gravity.CENTER or Gravity.TOP
                }
                else -> FrameLayout.LayoutParams( smallInt, smallInt).also {
//                    it.setMargins(0,0,paddingHorizontal,paddingVertical)
                    it.gravity = Gravity.RIGHT or Gravity.BOTTOM
                }
            }
            val tagView = ImageView(context)
//            GlideApp.with(context).load(mTags[i].url).into(tagView)
            GlideApp.with(context).load(tabStatics[i]).centerInside().into(tagView)
            addView(tagView, flp)
            mTagViews[i] = tagView
        }
        mTextView.text = mTags[1].name
    }

    fun setViewPager(viewPager: ViewPager){
        mViewPager = viewPager
        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
                Timber.d("state: $state")
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                Timber.d("position: $position | positionOffset: $positionOffset")
                val centerxypair = generateCenterXY(position, positionOffset)
                mTagViews[1]?.layoutParams!!.width = centerxypair[2].toInt()
                mTagViews[1]?.layoutParams!!.height = centerxypair[2].toInt()
                mTagViews[1]?.x = centerxypair[0]-mTagViews[1]?.width!!/2
                mTagViews[1]?.y = centerxypair[1]-mTagViews[1]?.width!!/2
                mTagViews[1]?.requestLayout()

                mTagViews[2]?.layoutParams!!.width = centerxypair[5].toInt()
                mTagViews[2]?.layoutParams!!.height = centerxypair[5].toInt()
                mTagViews[2]?.x = centerxypair[3]-mTagViews[2]?.width!!/2
                mTagViews[2]?.y = centerxypair[4]-mTagViews[2]?.width!!/2
                mTagViews[2]?.requestLayout()

                mTagViews[0]?.layoutParams!!.width = centerxypair[8].toInt()
                mTagViews[0]?.layoutParams!!.height = centerxypair[8].toInt()
                mTagViews[0]?.x = centerxypair[6]-mTagViews[0]?.width!!/2
                mTagViews[0]?.y = centerxypair[7]-mTagViews[0]?.width!!/2
                mTagViews[0]?.requestLayout()
            }

            override fun onPageSelected(position: Int) {
                Timber.d("selected: $position")
                mTextView.text = mTags[position].name
                when (position){
                    0 -> {
                        (mTagViews[0]?.layoutParams!! as LayoutParams).gravity = Gravity.TOP
                        (mTagViews[1]?.layoutParams!! as LayoutParams).gravity = Gravity.BOTTOM
                        (mTagViews[2]?.layoutParams!! as LayoutParams).gravity = Gravity.BOTTOM
                    }
                    1 -> {
                        (mTagViews[0]?.layoutParams!! as LayoutParams).gravity = Gravity.BOTTOM
                        (mTagViews[1]?.layoutParams!! as LayoutParams).gravity = Gravity.TOP
                        (mTagViews[2]?.layoutParams!! as LayoutParams).gravity = Gravity.BOTTOM
                    }
                    2-> {
                        (mTagViews[0]?.layoutParams!! as LayoutParams).gravity = Gravity.BOTTOM
                        (mTagViews[1]?.layoutParams!! as LayoutParams).gravity = Gravity.BOTTOM
                        (mTagViews[2]?.layoutParams!! as LayoutParams).gravity = Gravity.TOP
                    }
                }
            }

        })
        for (i in 0..2){
            mTagViews[i]?.setOnClickListener {
                mViewPager.setCurrentItem(i,true)
            }
        }
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
            collapseAnimator = ValueAnimator.ofInt(maxHeight!!, minHeight)
            collapseAnimator.addUpdateListener {
                val h= it.animatedValue as Int
                this.layoutParams.height = h
                this.requestLayout()
            }
            collapseAnimator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimating = false
                    isExpand = false
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    isAnimating = true
                    mTextView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out))
                }

            })
            collapseAnimator.duration = animDuration
            expandAnimator = ValueAnimator.ofInt(minHeight, maxHeight!!)
            expandAnimator.addUpdateListener {
                val h= it.animatedValue as Int
                this.layoutParams.height = h
                this.requestLayout()
            }
            expandAnimator.addListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    isAnimating = false
                    isExpand = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                    isAnimating = true
                    mTextView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in))
                }

            })
            expandAnimator.duration = animDuration
        }
//        initBottomY = mTagViews[0]?.y
        initBottomY = height-initSWidth!!
        initA = (initBottomY!!+initSWidth!!/2-initTopY!!-initLWidth!!/2)/initXhalfLength!!.pow(2)
    }

    private fun setBottomView(bottomView: View) {
        mBottomViewWrapper = WeakReference(bottomView)
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

        val xRightCenter = when (xCenter){
            in (initSWidth!!/2 .. (initMiddleX!!+initLWidth!!/2)) -> 2*initXhalfLength!!+initSWidth!!-(initXhalfLength!!+initSWidth!!-xCenter)
            else -> 2*initXhalfLength!!+initSWidth!!/2
        }
        val yRightCenter = initA!!*(initMiddleX!!+initLWidth!!/2 - xRightCenter).pow(2)+initLWidth!!/2
        val widthRight = when (position) {
//            0 -> initLWidth!! - (1f-positionOffset)*(initLWidth!!-initSWidth!!)
            1 -> initSWidth!! + (positionOffset)*(initLWidth!!-initSWidth!!)
            2 -> initLWidth!!
            else -> initSWidth!!
        }

        val xLeftCenter = when (xCenter){
            in ((initMiddleX!!+initLWidth!!/2)..2*initXhalfLength!!+initSWidth!!/2) -> initSWidth!!/2+(xCenter-initMiddleX!!-initLWidth!!/2)
            else -> initSWidth!!/2
        }
        val yLeftCenter = initA!!*(initMiddleX!!+initLWidth!!/2 - xLeftCenter).pow(2)+initLWidth!!/2
        val widthLeft = when (position) {
            0 -> initSWidth!! + (1f-positionOffset)*(initLWidth!!-initSWidth!!)
            else -> initSWidth!!
        }

        return arrayOf(xCenter,yCenter, width, xRightCenter, yRightCenter, widthRight, xLeftCenter, yLeftCenter, widthLeft)
    }

    fun addScrollChildFragment(fragment: Fragment){
        val recyclerView: RecyclerView? = fragment.view?.findViewById(R.id.recyclerView)
        recyclerView?.let {
            it.removeOnScrollListener(mOnScrollListener)
            it.addOnScrollListener(mOnScrollListener)
        }
    }

    var isExpand = true
    var isAnimating = false
    fun collapse(dy: Int){
        Timber.d("collapsing : $dy / $height ")
        if(isExpand and !isAnimating){
            collapseAnimator.start()
            mBottomViewWrapper.get()?.let {
                val slideDownAnimator = ValueAnimator.ofFloat(it.y,it.height+it.y)
                slideDownAnimator.addUpdateListener {
                    mBottomViewWrapper.get()?.y = it.animatedValue as Float
                }
                slideDownAnimator.start()
            }
        }

    }
    fun expand(dy: Int){
        if(!isExpand and !isAnimating){
            expandAnimator.start()
            mBottomViewWrapper.get()?.let {
                val slideDownAnimator = ValueAnimator.ofFloat(it.y,it.y-it.height)
                slideDownAnimator.addUpdateListener {
                    mBottomViewWrapper.get()?.y = it.animatedValue as Float
                }
                slideDownAnimator.start()
            }
        }
        Timber.d( " expanding : $dy / $height")
    }
}