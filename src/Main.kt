import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

const val INSIDE = 0
const val LEFT = 1
const val RIGHT = 2
const val BOTTOM = 4
const val TOP = 8

const val minX = 70
const val minY = 70
const val maxX = 330
const val maxY = 330

const val width = 500
const val height = 500

fun main() {
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val color = Color(0, 255, 0).rgb
    Draw.drawLine(minX, minY, maxX, minY, image)
    Draw.drawLine(maxX, minY, maxX, maxY, image)
    Draw.drawLine(maxX, maxY, minX, maxY, image)
    Draw.drawLine(minX, maxY, minX, minY, image)

    Draw.drawLine(5, 5, 185, 230, image)
    Draw.drawLine(30, 150, 480, 150, image)

    drawWithClipping(5, 5, 185, 230, image, color)
    drawWithClipping(30, 150, 480, 150, image, color)

    try {
        val file = File("image.png")
        ImageIO.write(image, "png", file)
    } catch (e: IOException) {
        println(e)
    }
}

fun drawWithClipping(x1: Int, y1: Int, x2: Int, y2: Int, image: BufferedImage, color: Int = -1) {
    var code1 = computeCode(x1, y1)
    var code2 = computeCode(x2, y2)
    var startX = x1
    var startY = y2
    var endX = x2
    var endY = y2

    var accept = false

    while (true) {
        if ((code1 == 0) && (code2 == 0)) {
            accept = true
            break
        } else
            if (code1 and code2 != 0) break
        else {
                val codeOut = if (code1 != 0) code1 else code2
                var x = 0
                var y = 0

                when {
                    codeOut and TOP != 0 -> {
                        x = x1 + (x2 - x1) * (maxY - y1) / (y2 - y1)
                        y = maxY
                    }
                    codeOut and BOTTOM != 0 -> {
                        x = x1 + (x2 - x1) * (minY - y1) / (y2 - y1)
                        y = minY
                    }
                    codeOut and RIGHT != 0 -> {
                        y = y1 + (y2 - y1) * (maxX - x1) / (x2 - x1)
                        x = maxX
                    }
                    codeOut and LEFT != 0 -> {
                        y = y1 + (y2 - y1) * (minX - x1) / (x2 - x1)
                        x = minX
                    }
                }

                if (codeOut == code1) {
                    startX = x
                    startY = y
                    code1 = computeCode(startX, startY)
                } else {
                    endX = x
                    endY = y
                    code2 = computeCode(endX, endY)
                }
            }
    }
    if (accept) Draw.drawLine(startX, startY, endX, endY, image, color)
}

class Point(
    val x: Int,
    val y: Int
)

fun computeCode(x: Int, y: Int): Int {
    var code = INSIDE

    if (x < minX) code = code or LEFT
    else if (x > maxX) code = code or RIGHT
    if (y < minY) code = code or BOTTOM
    else if (y > maxY) code = code or TOP

    return code
}