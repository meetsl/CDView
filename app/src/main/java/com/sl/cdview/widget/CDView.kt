package com.sl.cdview.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.sl.cdview.R
import org.jetbrains.anko.padding

/**
 * Created by shilong
 *  2018/5/29.
 */
class CDView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mBorderWidth = DEFAULT_OUTER_BORDER_WIDTH
    private var mInnerCircleRadius = 0
    private var mInnerPointRadius = 0
    private var mInnerBorderWidth = DEFAULT_INNER_BORDER_WIDTH
    private var mBitmapShader: BitmapShader? = null
    private var mBitmap: Bitmap? = null
    private var mBitmapHeight: Int = 0
    private var mBitmapWidth: Int = 0
    private var mBorderRadius: Float = 0.toFloat()
    private var mDrawableRadius: Float = 0.toFloat()
    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()
    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val mOuterBorderPaint = Paint()
    private val innerCirclePaint = Paint()
    private val innerPointPaint = Paint()
    private val innerShadePaint = Paint()
    private var mCDBgColor = Color.WHITE
    private var mShaderWidth = DEFAULT_SHADER_WIDTH
    private val mReady: Boolean
    private var mSetupPending: Boolean = false

    init {
        super.setScaleType(SCALE_TYPE)

        val a = context.obtainStyledAttributes(attrs, R.styleable.CDView, defStyleAttr, 0)
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CDView_outerBorderWidth, DEFAULT_OUTER_BORDER_WIDTH)
        mInnerBorderWidth = a.getDimensionPixelSize(R.styleable.CDView_innerBorderWidth, DEFAULT_INNER_BORDER_WIDTH)
        mShaderWidth = a.getDimensionPixelSize(R.styleable.CDView_cdShaderWidth, DEFAULT_SHADER_WIDTH)
        mInnerCircleRadius = a.getDimensionPixelSize(R.styleable.CDView_innerCircleRadius, 0)
        mInnerPointRadius = a.getDimensionPixelSize(R.styleable.CDView_innerPointRadius, 0)
        mCDBgColor = a.getColor(R.styleable.CDView_cdBgColor, Color.WHITE)
        a.recycle()

        mReady = true

        if (mSetupPending) {
            init()
            mSetupPending = false
        }
    }

    private fun init() {

        if (!mReady) {
            mSetupPending = true
            return
        }

        if (mBitmap == null) {
            return
        }

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader

        mOuterBorderPaint.isAntiAlias = true
        mOuterBorderPaint.color = mCDBgColor
        mOuterBorderPaint.setShadowLayer(mShaderWidth.toFloat(), 0f, 0f, Color.GRAY)

        innerCirclePaint.style = Paint.Style.STROKE
        innerCirclePaint.isAntiAlias = true
        innerCirclePaint.color = mCDBgColor
        innerCirclePaint.strokeWidth = mInnerBorderWidth.toFloat()

        innerPointPaint.style = Paint.Style.FILL
        innerPointPaint.isAntiAlias = true
        innerPointPaint.color = mCDBgColor


        innerShadePaint.style = Paint.Style.STROKE
        innerShadePaint.isAntiAlias = true
        innerShadePaint.color = mCDBgColor
        innerShadePaint.strokeWidth = 1f
        innerShadePaint.setShadowLayer(3f, 0f, 2f, Color.GRAY)

        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width

        mBorderRect.set(0f, 0f, width.toFloat(), height.toFloat())
        mBorderRadius = Math.min((mBorderRect.height() - mShaderWidth - 10) / 2, (mBorderRect.width() - mShaderWidth - 10) / 2)

        mDrawableRect.set(mBorderWidth.toFloat(), mBorderWidth.toFloat(), mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth)
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2)

        updateShaderMatrix()
        invalidate()
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(((dx + 0.5f).toInt() + mBorderWidth).toFloat(), ((dy + 0.5f).toInt() + mBorderWidth).toFloat())

        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        init()
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null) {
            return
        }
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mBorderRadius, mOuterBorderPaint)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mDrawableRadius, mBitmapPaint)
        mInnerCircleRadius = if (mInnerCircleRadius == 0) (mBorderRadius / 5).toInt() else mInnerCircleRadius
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mInnerCircleRadius.toFloat(), innerCirclePaint)
        mInnerPointRadius = if (mInnerPointRadius == 0) (mBorderRadius / 7).toInt() else mInnerPointRadius
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mInnerPointRadius.toFloat(), innerPointPaint)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mInnerPointRadius.toFloat(), innerShadePaint)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        mBitmap = bm
        init()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        init()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(drawable)
        init()
    }

    fun setCDBgColor(cdBgColor: Int) {
        if (cdBgColor == mCDBgColor) {
            return
        }

        mCDBgColor = cdBgColor
        mOuterBorderPaint.color = mCDBgColor
        innerCirclePaint.color = mCDBgColor
        innerPointPaint.color = mCDBgColor
        innerShadePaint.color = mCDBgColor
        invalidate()
    }

    fun setCDShaderWidth(shaderWidth: Int) {
        if (mShaderWidth == shaderWidth) {
            return
        }
        mShaderWidth = shaderWidth
        init()
    }

    fun setOuterBorderWidth(borderWidth: Int) {
        if (borderWidth == mBorderWidth) {
            return
        }
        mBorderWidth = borderWidth
        init()
    }

    fun setInnerBorderWidth(borderWidth: Int) {
        if (borderWidth == mInnerBorderWidth) {
            return
        }
        mInnerBorderWidth = borderWidth
        init()
    }

    fun setInnerCircleRadius(radius: Int) {
        if (radius == mInnerCircleRadius)
            return
        mInnerCircleRadius = radius
        init()
    }

    fun setInnerPointRadius(radius: Int) {
        if (radius == mInnerPointRadius)
            return
        mInnerPointRadius = radius
        init()
    }

    fun startRotateAnimation() {
        startAnimation(rotateAnimation())
    }

    private fun rotateAnimation(): Animation? {
        val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.repeatMode = Animation.INFINITE
        rotateAnimation.repeatCount = Int.MAX_VALUE
        rotateAnimation.duration = 12000
        return rotateAnimation
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        return try {
            val bitmap: Bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: OutOfMemoryError) {
            null
        }

    }

    companion object {
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private val SCALE_TYPE = ImageView.ScaleType.CENTER_CROP
        private const val COLOR_DRAWABLE_DIMENSION = 1
        private const val DEFAULT_OUTER_BORDER_WIDTH = 40
        private const val DEFAULT_INNER_BORDER_WIDTH = 10
        private const val DEFAULT_SHADER_WIDTH = 10
    }
}