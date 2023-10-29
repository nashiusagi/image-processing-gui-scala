package img.filtering

import java.lang.Math
import scala.collection.immutable.ArraySeq

class GaussianFilter(kSize: Int, sigma: Double) extends Filter(kSize) {
  override val kernel: Seq[Double] = {
    for (y <- (0 until kernelSize); x <- (0 until kernelSize))
      yield {
        Math.exp(
          -((x - paddingSize) * (x - paddingSize) + (y - paddingSize) * (y - paddingSize))
        ) / (2.0 * sigma * sigma)
      }
  }.to(ArraySeq)
}
