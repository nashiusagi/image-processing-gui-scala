package img

import scalafx.scene.paint.Color

class Pixel(
    coordX: Int,
    coordY: Int,
    argbData: Int
) {
  val x: Int = coordX
  val y: Int = coordY
  val color: Int = argbData

  def getColor(): Color = {
    val alpha: Double = ((color >> 24) & 0xff) / 255.0
    val r: Double = ((color & 0x00ff0000) >> 16) / 255.0
    val g: Double = ((color & 0x0000ff00) >> 8) / 255.0
    val b: Double = (color & 0x000000ff) / 255.0

    val javafxColor: javafx.scene.paint.Color =
      new javafx.scene.paint.Color(r, g, b, alpha)

    new Color(javafxColor)
  }

  val red: Double =
    ((color & 0x00ff0000) >> 16) / 255.0

  val green: Double =
    ((color & 0x0000ff00) >> 8) / 255.0

  val blue: Double = (color & 0x000000ff) / 255.0

  def grayScale(): Pixel = {
    val gray: Double = 0.2126 * red + 0.7152 * green + 0.0722 * blue
    val newColor =
      0xff000000 + ((gray * 255).toInt << 16) + ((gray * 255).toInt << 8) + (gray * 255).toInt

    new Pixel(x, y, newColor)

  }

  def +(that: Pixel): Pixel = {
    require(this.x == that.x)
    require(this.y == that.y)

    val sumRed = Math.abs(this.red + that.red)
    val sumGreen = Math.abs(this.green + that.green)
    val sumBlue = Math.abs(this.blue + that.blue)

    val newColor =
      0xff000000 + ((sumRed * 255).toInt << 16) + ((sumGreen * 255).toInt << 8) + (sumBlue * 255).toInt

    new Pixel(x, y, newColor)

  }

  def -(that: Pixel): Pixel = {
    require(this.x == that.x)
    require(this.y == that.y)

    val diffRed = Math.abs(this.red - that.red)
    val diffGreen = Math.abs(this.green - that.green)
    val diffBlue = Math.abs(this.blue - that.blue)

    val newColor =
      0xff000000 + ((diffRed * 255).toInt << 16) + ((diffGreen * 255).toInt << 8) + (diffBlue * 255).toInt

    new Pixel(x, y, newColor)
  }

  def scaling(scalar: Double): Pixel = {
    val scaledRed = scalar * this.red
    val scaledGreen = scalar * this.green
    val scaledBlue = scalar * this.blue

    val newColor =
      0xff000000 + ((scaledRed * 255).toInt << 16) + ((scaledGreen * 255).toInt << 8) + (scaledBlue * 255).toInt

    new Pixel(x, y, newColor)
  }

  def normalize(maxR: Double, maxG: Double, maxB: Double): Pixel = {
    val normRed = this.red / maxR
    val normGreen = this.green / maxG
    val normBlue = this.blue / maxB

    val newColor =
      0xff000000 + ((normRed * 255).toInt << 16) + ((normGreen * 255).toInt << 8) + (normBlue * 255).toInt

    new Pixel(x, y, newColor)
  }
}
