package cn.kirilenkoo.www.coolpets.ui.view

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.kirilenkoo.www.coolpets.R
import cn.kirilenkoo.www.coolpets.model.Tag
import timber.log.Timber
import java.lang.ref.WeakReference

class CoolPetHeaderView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var mTags: List<Tag>
    private lateinit var mViewPager: ViewPager
    private val mOnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if(dy>0){//scroll down
                expand(dy)
            }else{// scroll up
                collapse(dy)
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
    }
    fun setViewPager(viewPager: ViewPager){
        mViewPager = viewPager
        mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Timber.d("position: $position | positionOffset: $positionOffset | offsetPixels: $positionOffsetPixels")
            }

            override fun onPageSelected(position: Int) {
            }

        })
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
    }
    fun expand(dy: Int){
        Timber.d( " expanding : $dy / $height")
    }
}