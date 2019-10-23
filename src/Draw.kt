import java.awt.image.BufferedImage
import kotlin.math.abs

object Draw {
    fun drawLine(_x0: Int, _y0: Int, _x1: Int, _y1: Int, image: BufferedImage, color: Int = -1) {
        var x0 = _x0
        var y0 = _y0
        var x1 = _x1
        var y1 = _y1
        var steep = false

        if (abs(x0-x1) < abs(y0-y1)) {
            x0 = y0.also { y0 = x0 }
            x1 = y1.also { y1 = x1 }
            steep = true
        }

        if (x0 > x1) {
            x0 = x1.also { x1 = x0 }
            y0 = y1.also { y1 = y0 }
        }

        val dx = x1 - x0
        val dy = y1 - y0
        val deltaError = abs(dy) * 2
        var error = 0
        var y = y0
        for (x in x0..x1) {
            if (steep) {
                drawPixel(y, x, image, color)
            } else {
                drawPixel(x, y, image, color)
            }
            error += deltaError

            if (error > dx) {
                y += if (y1 > y0) 1 else -1
                error -= dx*2
            }
        }
    }

    private fun drawPixel(x: Int, y: Int, image: BufferedImage, rgb: Int = -1) {
        if (x < 0 || y < 0 || x >= image.width || y >= image.height)
            return println("Пиксель в координате ($x, $y) не нарисован: выход за границу")

        image.setRGB(x, y, rgb)
    }
}