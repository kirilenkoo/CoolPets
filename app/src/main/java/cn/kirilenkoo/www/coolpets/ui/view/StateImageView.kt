package cn.kirilenkoo.www.coolpets.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import cn.kirilenkoo.www.coolpets.R

class StateImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr)  {
    private var mProgress: Int = 0

    enum class State {
        INIT,
        SUCCESS,
        FAIL,
        INPROGRESS
    }

    private var mState = State.INIT

//    internal var progressCircleMaxSize: Int = 0

    private var mHasInitMask = false


    private var circleStrokeWidth = 2

    init {
        scaleType = ScaleType.CENTER_CROP
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (mState) {
            State.INIT -> if (mHasInitMask)
                drawMask(canvas)
            State.SUCCESS -> {
            }
            State.FAIL -> {
                drawMask(canvas)
                drawProgress(canvas, 0)
                drawErrorMark(canvas)
                drawErrorText("点击重发", canvas)
            }
            State.INPROGRESS -> {
                drawMask(canvas)
                drawProgress(canvas, mProgress)
                drawProgressText(canvas, mProgress)
            }
        }
    }

    private fun drawProgressText(canvas: Canvas, mProgress: Int) {
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width / 6, height / 6)
        val whitePaint = Paint()
        whitePaint.color = Color.WHITE
        whitePaint.textAlign = Paint.Align.CENTER
        whitePaint.textSize = radius / 2f
        canvas.drawText(mProgress.toString() + "%", centerX.toFloat(), centerY + radius / 4f, whitePaint)
    }

    private fun drawErrorText(text: String, canvas: Canvas) {
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width / 6, height / 6)
        val whitePaint = Paint()
        whitePaint.color = Color.WHITE
        whitePaint.textAlign = Paint.Align.CENTER
        whitePaint.textSize = (radius / 2).toFloat()
        canvas.drawText(text, centerX.toFloat(), (centerY + radius * 2).toFloat(), whitePaint)
    }

    private fun drawErrorMark(canvas: Canvas) {
        val d = ContextCompat.getDrawable(context, R.drawable.icon_ex_mark)
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width / 9, height / 9)
        //        canvas.drawCircle(getWidth()/2,getHeight()/2, radius ,greyPaint);
        d?.setBounds(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        d?.draw(canvas)
    }

    private fun drawProgress(canvas: Canvas, progress: Int) {
        val greyPaint = Paint()
        greyPaint.color = ContextCompat.getColor(context, R.color.material_grey_600)
        greyPaint.style = Paint.Style.STROKE
        greyPaint.strokeWidth = circleStrokeWidth.toFloat()
        greyPaint.isAntiAlias = true
        val bluePaint = Paint()
        bluePaint.style = Paint.Style.STROKE
        bluePaint.strokeWidth = circleStrokeWidth.toFloat()
        bluePaint.color = resources.getColor(R.color.colorPrimary)
        bluePaint.isAntiAlias = true
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width / 6, height / 6)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius.toFloat(), greyPaint)
        val rf = RectF((centerX - radius).toFloat(), (centerY - radius).toFloat(), (centerX + radius).toFloat(), (centerY + radius).toFloat())
        canvas.drawArc(rf, -90f, (360 * progress / 100).toFloat(), false, bluePaint)
    }

    private fun drawMask(canvas: Canvas) {
        canvas.drawColor(resources.getColor(R.color.black_mask_color))
    }

    fun setmState(state: State) {
        mState = state
        invalidate()
    }

    fun getState(): State {
        return mState
    }

    fun setProgress(progress: Int) {
        mProgress = progress
        invalidate()
    }
}