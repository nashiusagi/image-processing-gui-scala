package img.saliencyMap

import img.Image
import img.filtering.GaussianFilter
import img.BilinearInterpolation

class SaliencyMap() {
  val gaussianFilter = new GaussianFilter(3, 1.3)

  def makePyramid(orig: Image): Seq[Image] = {
    val pyramid: Seq[Image] = for (i <- (1 to 6)) yield {
      val upScale: Double = Math.pow(2, i).toDouble
      val downScale: Double = 1 / upScale
      val downScaleInterpolation =
        new BilinearInterpolation(downScale, downScale)
      val upScaleInterpolation = new BilinearInterpolation(upScale, upScale)

      val downscaledImage: Image = downScaleInterpolation.interpolate(orig)
      val gaussian = gaussianFilter.filtering(downscaledImage)
      val upScaledImage: Image =
        upScaleInterpolation.interpolate(gaussian)

      upScaledImage
    }

    orig +: pyramid
  }

  def saliencyMap(orig: Image): Image = {
    val pyramid = makePyramid(orig)
    val out: Image =
      (pyramid(0) - pyramid(1))
        .+(pyramid(0) - pyramid(2))
        .+(pyramid(1) - pyramid(2))
        .+(pyramid(2) - pyramid(3))

    out.normalize()
  }
}
