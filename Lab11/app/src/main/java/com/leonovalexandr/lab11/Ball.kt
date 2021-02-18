package com.leonovalexandr.lab11

import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

// Класс шарика.
class Ball {

    var radius = 50f

    // Координата x шара.
    var cx = 0f

    // Координата y шара.
    var cy = 0f

    // Скорость по оси x.
    var dx = 10f

    // Скорость по оси y.
    var dy = 10f

    fun move(height: Int, width: Int){
        wallCollision(height, width)
        cx += dx
        cy += dy
    }

    private fun wallCollision(height: Int, width: Int) {
        if (cx > width - radius || cx < radius)
            dx = -dx
        if (cy > height - radius || cy < radius)
            dy = -dy
    }

    fun ballCollision(ball: Ball) {
        val distance = sqrt((cx - ball.cx).toDouble().pow(2) + (cy - ball.cy).toDouble().pow(2))
        if (distance < (radius + ball.radius) * 1.05) {
            val newdx = (dx.absoluteValue + ball.dx.absoluteValue)
            val newdy = (dy.absoluteValue + ball.dy.absoluteValue)

            val newdx1 = newdx * (radius) / (radius + ball.radius)
            val newdx2 = newdx * (ball.radius) / (radius + ball.radius)

            val newdy1 = newdy * (radius) / (radius + ball.radius)
            val newdy2 = newdy * (ball.radius) / (radius + ball.radius)

            if (dx * ball.dx > 0){
                if (dx < 0){
                    dx = -newdx1
                    ball.dx = -newdx2
                } else {
                    dx = newdx1
                    ball.dx = newdx2
                }
            } else {
                if (dx < 0) {
                    dx = newdx1
                    ball.dx = -newdx2
                } else {
                    dx = -newdx1
                    ball.dx = +newdx2
                }
            }

            if (dy * ball.dy > 0){
                if (dy < 0){
                    dy = -newdy1
                    ball.dy = -newdy2
                } else {
                    dy = newdy1
                    ball.dy = newdy2
                }
            } else {
                if (dy < 0) {
                    dy = newdy1
                    ball.dy = -newdy2
                } else {
                    dy = -newdy1
                    ball.dy = +newdy2
                }
            }
        }
    }
}