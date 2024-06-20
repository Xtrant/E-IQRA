package com.example.e_iqra.view.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var path = Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    private val drawColor = Color.BLACK
    private val backgroundColor = Color.WHITE
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var storedBitmap: Bitmap? = null

    init {
        setupDrawing()
    }

    private fun setupDrawing() {
        paint.color = drawColor
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        storedBitmap?.let {
            extraCanvas.drawBitmap(it, 0f, 0f, null)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (::extraBitmap.isInitialized) {
            canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(touchX, touchY)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(touchX, touchY)
                extraCanvas.drawPath(path, paint)
            }
            MotionEvent.ACTION_UP -> {
                path.reset()
                storedBitmap = Bitmap.createBitmap(extraBitmap)
            }
            else -> return false
        }

        invalidate()
        return true
    }

    fun setBitmap(bitmap: Bitmap) {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(mutableBitmap.width, mutableBitmap.height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)

        extraCanvas.drawBitmap(mutableBitmap, 0f, 0f, null)

        invalidate()
    }

    fun getBitmap(): Bitmap {
        return if (::extraBitmap.isInitialized) {
            extraBitmap
        } else {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        }
    }

    fun clearCanvas() {
        if (!::extraCanvas.isInitialized || !::extraBitmap.isInitialized) {
            return
        }
        path.reset()
        extraCanvas.drawColor(backgroundColor)
        storedBitmap = null
        invalidate()
    }
}
