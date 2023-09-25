package img

import _root_.img.filtering.GaussianFilter

class SaliencyMap(src: Image) {
  val gray: Image = src
  val width: Int = src.width
  val height: Int = src.height

  val gaussianFilter = new GaussianFilter(3, 1.3)

  def makePyramid(): List[Image] = {
    val pyramid: List[Image] = for (i <- (1 to 6).toList) yield {
      val upScale: Double = Math.pow(2, i).toDouble
      val downScale: Double = 1 / upScale
      val downScaleInterpolation =
        new BilinearInterpolation(downScale, downScale)
      val upScaleInterpolation = new BilinearInterpolation(upScale, upScale)

      val downscaledImage: Image = downScaleInterpolation.interpolate(gray)
      val gaussian = gaussianFilter.filtering(downscaledImage)
      val upScaledImage: Image =
        upScaleInterpolation.interpolate(gaussian)

      upScaledImage
    }

    gray :: pyramid
  }

  def saliencyMap(): Image = {
    val pyramid = makePyramid()
    val out: Image =
      (pyramid(0) - pyramid(1))
        .+(pyramid(0) - pyramid(2))
        .+(pyramid(1) - pyramid(2))
        .+(pyramid(2) - pyramid(3))

    out.normalize()
  }
}
