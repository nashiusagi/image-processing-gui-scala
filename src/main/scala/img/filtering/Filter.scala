package img.filtering

import img.Image
import img.Pixel

class Filter(kSize: Int) {
  val kernelSize: Int = kSize
  val paddingSize: Int = kernelSize / 2

  val kernel: List[Double] =
    for (i <- (0 until kernelSize * kernelSize).toList)
      yield 1.0 / (kernelSize * kernelSize)

  def filtering(src: Image): Image = {
    val paddedImg: Image = padding(src)

    val filteredPixels: List[Pixel] =
      for (
        y <- (0 until paddedImg.height).toList;
        x <- (0 until paddedImg.width).toList
      ) yield {
        if (
          y < paddingSize || y > src.height + paddingSize - 1 || x < paddingSize || x > src.width + paddingSize - 1
        ) new Pixel(x, y, 0xff000000)
        else {
          val sumRGB = fold(kernel, paddedImg, x, y)
          val sumR = sumRGB(0)
          val sumG = sumRGB(1)
          val sumB = sumRGB(2)

          val newColor =
            0xff000000 + ((sumR * 255).toInt << 16) + ((sumG * 255).toInt << 8) + (sumB * 255).toInt

          new Pixel(
            x,
            y,
            newColor
          )
        }
      }

    val filtered: Image =
      new Image(filteredPixels, paddedImg.width, paddedImg.height)

    val out: Image = unpadding(filtered)

    out
  }

  def fold(
      kernel: List[Double],
      paddedImg: Image,
      x: Int,
      y: Int
  ): List[Double] = {
    val sumR: Double = (0 until kernelSize * kernelSize).toList
      .map(idx =>
        kernel(idx) * paddedImg
          .getPixel(
            x + idx % kernelSize - paddingSize,
            y + idx / kernelSize - paddingSize
          )
          .red
      )
      .foldLeft(0.0)(_ + _)

    val sumG: Double = (0 until kernelSize * kernelSize).toList
      .map(idx =>
        kernel(idx) * paddedImg
          .getPixel(
            x + idx % kernelSize - paddingSize,
            y + idx / kernelSize - paddingSize
          )
          .green
      )
      .foldLeft(0.0)(_ + _)

    val sumB: Double = (0 until kernelSize * kernelSize).toList
      .map(idx =>
        kernel(idx) * paddedImg
          .getPixel(
            x + idx % kernelSize - paddingSize,
            y + idx / kernelSize - paddingSize
          )
          .blue
      )
      .foldLeft(0.0)(_ + _)

    List(sumR, sumG, sumB).map(c => clip(c, 0.0, 1.0))
  }

  def padding(src: Image): Image = {
    val paddedWidth = src.width + 2 * paddingSize
    val paddedHeight = src.height + 2 * paddingSize

    val paddedPixels =
      for (
        y <- (0 until paddedHeight).toList; x <- (0 until paddedWidth).toList
      )
        yield {
          if (
            y < paddingSize || y > src.height + paddingSize - 1 || x < paddingSize || x > src.width + paddingSize - 1
          ) new Pixel(x, y, 0xff000000)
          else
            new Pixel(
              x,
              y,
              src
                .pixels((y - paddingSize) * src.width + (x - paddingSize))
                .color
            )

        }

    new Image(paddedPixels, paddedWidth, paddedHeight)
  }

  def unpadding(src: Image): Image = {
    val unpaddedWidth = src.width - 2 * paddingSize
    val unpaddedHeight = src.height - 2 * paddingSize

    val unpaddedPixels =
      for (
        y <- (paddingSize until src.height - paddingSize).toList;
        x <- (paddingSize until src.width - paddingSize).toList
      )
        yield {
          new Pixel(
            x - paddingSize,
            y - paddingSize,
            src
              .pixels(y * src.width + x)
              .color
          )
        }

    new Image(unpaddedPixels, unpaddedWidth, unpaddedHeight)
  }

  def clip(x: Double, floor: Double, ceil: Double) = {
    if (x > floor && x < ceil) x
    else if (x <= floor) floor
    else ceil
  }
}
