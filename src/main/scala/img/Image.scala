package img

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class Image(pixs: Seq[Pixel], w: Int, h: Int) {
  val width: Int = w
  val height: Int = h
  val pixels: Seq[Pixel] = pixs
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

  def +(that: Image): Image = {
    require(width == that.width)
    require(height == that.height)

    val sumPixels: Seq[Pixel] =
      for (i <- (0 until width * height))
        yield {
          val src = this.pixels(i)
          val dst = that.pixels(i)
          src + dst
        }

    new Image(sumPixels, width, height)
  }

  def -(that: Image): Image = {
    require(width == that.width)
    require(height == that.height)

    val diffPixels: Seq[Pixel] =
      for (i <- (0 until width * height))
        yield {
          val src = this.pixels(i)
          val dst = that.pixels(i)
          src - dst
        }

    new Image(diffPixels, width, height)
  }

  def scaling(scalar: Double): Image = {
    val scaledPixels: Seq[Pixel] =
      for (pix <- this.pixels)
        yield {
          pix.scaling(scalar)
        }

    new Image(scaledPixels, width, height)
  }

  def grayScale(): Image = {
    val grayscalePixels: Seq[Pixel] = for (pix <- pixels) yield pix.grayScale()

    new Image(grayscalePixels, width, height)
  }

  def normalize(): Image = {
    // red == green == blue if grayscale
    val grayColorList: Seq[Double] = for (pix <- pixels) yield pix.red

    val maxGrayColor = grayColorList.max

    val normalizedPixels: Seq[Pixel] =
      for (pix <- pixels)
        yield pix.normalize(maxGrayColor, maxGrayColor, maxGrayColor)

    new Image(normalizedPixels, width, height)
  }

  def grayScale2Jet(): Image = {
    val jetPixels: Seq[Pixel] =
      for (pix <- pixels) yield pix.grayscale2Jet(0.0, 1.0)

    new Image(jetPixels, width, height)
  }
}
