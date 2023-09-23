package img.filtering

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams

class GaussianFilterSpec extends AnyFlatSpec with Diagrams {
  val gaussianFilter = new GaussianFilter(3, 1.3)

  "gaussian kernel" should "正しい値" in {
    assert(
      gaussianFilter.kernel == List(0.04004002462621677, 0.10884007135249772,
        0.04004002462621677, 0.10884007135249772, 0.29585798816568043,
        0.10884007135249772, 0.04004002462621677, 0.10884007135249772,
        0.04004002462621677)
    )
  }
}
