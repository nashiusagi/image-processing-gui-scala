package img.saliencyMap

import img._

class ittiSaliencyMap() extends SaliencyMap() {
  def calcRImage(src: Image): Image = {
    val rPixels: List[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = pixel.red - (pixel.green + pixel.blue) / 2
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(rPixels, src.width, src.height)
  }

  def calcGImage(src: Image): Image = {
    val gPixels: List[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = pixel.green - (pixel.red + pixel.blue) / 2
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(gPixels, src.width, src.height)
  }

  def calcBImage(src: Image): Image = {
    val bPixels: List[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = pixel.blue - (pixel.red + pixel.green) / 2
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(bPixels, src.width, src.height)
  }

  def calcYImage(src: Image): Image = {
    val yPixels: List[Pixel] = for (pixel <- src.pixels) yield {
      val tmpValue = (pixel.red + pixel.green) / 2 - Math.abs(
        pixel.red - pixel.green
      ) / 2 - pixel.blue
      val newValue = Math.max(tmpValue, 0.0)
      val newColor =
        0xff000000 + ((newValue * 255).toInt << 16) + ((newValue * 255).toInt << 8) + (newValue * 255).toInt

      new Pixel(pixel.x, pixel.y, newColor)
    }

    new Image(yPixels, src.width, src.height)
  }

  def calcColorSaliencyMap(src: Image): (Image, Image) = {
    val rImage = calcRImage(src)
    val gImage = calcGImage(src)
    val bImage = calcBImage(src)
    val yImage = calcYImage(src)

    val rPyramid = makePyramid(rImage)
    val gPyramid = makePyramid(gImage)
    val bPyramid = makePyramid(bImage)
    val yPyramid = makePyramid(yImage)

    val rgMap =
      ((rPyramid(0) - gPyramid(0)) - (rPyramid(1) - gPyramid(1)))
        .+((rPyramid(0) - gPyramid(0)) - (rPyramid(2) - gPyramid(2)))
        .+((rPyramid(1) - gPyramid(1)) - (rPyramid(2) - gPyramid(2)))
        .+((rPyramid(2) - gPyramid(2)) - (rPyramid(3) - gPyramid(3)))

    val byMap =
      ((bPyramid(0) - yPyramid(0)) - (bPyramid(1) - yPyramid(1)))
        .+((bPyramid(0) - yPyramid(0)) - (bPyramid(2) - yPyramid(2)))
        .+((bPyramid(1) - yPyramid(1)) - (bPyramid(2) - yPyramid(2)))
        .+((bPyramid(2) - yPyramid(2)) - (bPyramid(3) - yPyramid(3)))

    (rgMap, byMap)
  }
}
