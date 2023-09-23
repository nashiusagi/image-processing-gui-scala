package img.filtering

import java.lang.Math

class GaussianFilter(kSize: Int, sigma: Double) extends Filter(kSize) {
  override val kernel: List[Double] = {
    for (y <- (0 until kernelSize).toList; x <- (0 until kernelSize).toList)
      yield {
        Math.exp(
          -((x - paddingSize) * (x - paddingSize) + (y - paddingSize) * (y - paddingSize))
        ) / (2.0 * sigma * sigma)
      }
  }
}
