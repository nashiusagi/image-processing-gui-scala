package img

class BilinearInterpolation(rx: Double, ry: Double) {
  val ax = rx
  val ay = ry

  def interpolate(src: Image): Image = {
    val srcWidth: Int = src.width
    val srcHeight: Int = src.height

    // get position of resized image
    val aH: Int = (ay * srcHeight).toInt
    val aW: Int = (ax * srcWidth).toInt

    // output image
    val interpolatedPixels: Seq[Pixel] =
      for (y <- (0 until aH); x <- (0 until aW)) yield {
        val yBefore = Math.min(Math.floor(y / ay).toInt, srcHeight - 2)
        val dy: Double = ((y / ay).toInt - yBefore).toDouble

        val xBefore = Math.min(Math.floor(x / ax).toInt, srcWidth - 2)
        val dx: Double = ((x / ax).toInt - xBefore).toDouble

        // compute bi-linear
        val red: Double = (1.0 - dx) * (1.0 - dy) * src
          .getPixel(xBefore, yBefore)
          .red + dx * (1.0 - dy) * src
          .getPixel(xBefore + 1, yBefore)
          .red + (1.0 - dx) * dy * src
          .getPixel(xBefore, yBefore + 1)
          .red + dx * dy * src.getPixel(xBefore + 1, yBefore + 1).red

        val green: Double = (1 - dx) * (1 - dy) * src
          .getPixel(xBefore, yBefore)
          .green + dx * (1 - dy) * src
          .getPixel(xBefore + 1, yBefore)
          .green + (1 - dx) * dy * src
          .getPixel(xBefore, yBefore + 1)
          .green + dx * dy * src.getPixel(xBefore + 1, yBefore + 1).green

        val blue: Double = ((1.0 - dx) * (1.0 - dy) * src
          .getPixel(xBefore, yBefore)
          .blue) + (dx * (1.0 - dy) * src
          .getPixel(xBefore + 1, yBefore)
          .blue) + ((1.0 - dx) * dy * src
          .getPixel(xBefore, yBefore + 1)
          .blue) + (dx * dy * src.getPixel(xBefore + 1, yBefore + 1).blue)

        val newColor =
          0xff000000 + ((red * 255).toInt << 16) + ((green * 255).toInt << 8) + (blue * 255).toInt

        new Pixel(x, y, newColor)
      }

    new Image(interpolatedPixels, aW, aH)
  }
}
