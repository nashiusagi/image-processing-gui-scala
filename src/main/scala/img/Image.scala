package img

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class Image(pixs: List[Pixel], w: Int, h: Int) {
  val width: Int = w
  val height: Int = h
  val pixels: List[Pixel] = pixs
  val image = pixels2BufferedImage()

  def pixels2BufferedImage(): BufferedImage = {
    val bufImage: BufferedImage =
      new BufferedImage(width, height, TYPE_INT_ARGB)

    pixels.foreach(pixel => bufImage.setRGB(pixel.x, pixel.y, pixel.color))

    bufImage
  }

  def getPixel(x: Int, y: Int): Pixel = {
    pixels(y * width + x)
  }
}
