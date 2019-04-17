package com.theapache64.leericos

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.animation.AccelerateDecelerateInterpolator
import com.theapache64.twinkill.logger.info

class ShootingStarView(context: Context?, attrs: AttributeSet?) : SurfaceView(context, attrs), SurfaceHolder.Callback {

    companion object {
        private val LINE_COLOR = Color.BLACK
        private const val LINE_WIDTH = 5f
    }

    private var phase = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = LINE_COLOR
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = LINE_WIDTH
    }

    init {
        holder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceCreated(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        startAnimating()
    }

    private fun startAnimating() {

        path = createRandomPath()

        val nextAnimDelay = (2000..10000).random().toLong()
        val animDuration = (1000..3000).random().toLong()

        // Starting phase animation
        val animator = ObjectAnimator.ofFloat(this, "phase", 1f, 0f, -1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                println("Animation ended")
                postDelayed({
                    startAnimating()
                }, nextAnimDelay)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
        animator.start()
    }

    private fun setPhase(phase: Float) {
        this.phase = phase
        info("Phase is $phase")
        drawIt()
    }

    lateinit var path: Path

    private fun drawIt() {
        holder.lockCanvas()!!.let { canvas ->

            // Drawing white background
            canvas.drawColor(Color.WHITE)

            // Creating path
            val dashPath = createPathEffect(path)
            paint.pathEffect = dashPath

            // Drawing path (line)
            canvas.drawPath(path, paint)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun createRandomPath(): Path {

        val widthRange = 0..(width / 2)
        val heightRange = 0..(height / 2)

        val startX = widthRange.random().toFloat()
        val startY = heightRange.random().toFloat()

        val endX = widthRange.random().toFloat()
        val endY = heightRange.random().toFloat()

        return Path().apply {
            moveTo(startX, startY)
            lineTo(endX, endY)
        }
    }

    private fun createPathEffect(path: Path): DashPathEffect {
        val pathLength = PathMeasure(path, true).length
        return DashPathEffect(floatArrayOf(pathLength, pathLength), pathLength * phase)
    }

}