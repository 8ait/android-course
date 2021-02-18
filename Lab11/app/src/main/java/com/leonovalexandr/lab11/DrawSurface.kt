package com.leonovalexandr.lab11

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

class DrawSurface: SurfaceView, SurfaceHolder.Callback {

    private var radius: Float = 100f

    private var paint = Paint()
    private lateinit var job: Job

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        paint.color = Color.WHITE
        var balls = ArrayList<Ball>()
        val rand = Random(0)
        for (i in 1..2) {
            for (j in 1..3) {
                val ball = Ball()
                ball.cx = width / 3 * i.toFloat()
                ball.cy = height / 4 * j.toFloat()

                ball.dx = rand.nextInt(-5, 5).toFloat()
                ball.dy = rand.nextInt(-5, 5).toFloat()

                ball.radius = rand.nextInt(90, 150).toFloat()

                balls.add(ball)
            }
        }
        job = GlobalScope.launch {
            var canvas: Canvas?
            while (true) {
                canvas = holder.lockCanvas(null)
                if (canvas != null) {
                    canvas.drawColor(Color.argb(255, 0, 192, 192))
                    balls.forEach{
                        ball -> canvas.drawCircle(ball.cx.toFloat(), ball.cy.toFloat(), ball.radius, paint)
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
                // Перемещение шарика
                balls.forEach{
                    ball -> ball.move(height, width)
                }

                for (i in 0 until balls.size) {
                    for (j in i + 1 until balls.size)
                        balls[i].ballCollision(balls[j])
                }
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        job.cancel()
    }

    init { holder.addCallback(this) }
}
