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
}
