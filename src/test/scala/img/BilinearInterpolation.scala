package img

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class BilinearInterpolationSpec extends AnyFlatSpec with Diagrams {
  val bufImage: BufferedImage = new BufferedImage(128, 128, TYPE_INT_ARGB)

  val width: Int = bufImage.getWidth
  val height: Int = bufImage.getHeight

  val pixels: List[Pixel] =
    for (iter <- (0 until width * height).toList) yield {
      val x = iter % width
      val y = iter / height
      val color = bufImage.getRGB(x, y)

      new Pixel(x, y, color)
    }
  val image = new Image(pixels, width, height)

  "補完後画像サイズ" should "rx, ryと元画像サイズをかけ合わせたものになっている" in {
    val bilinearInterpolation = new BilinearInterpolation(1.5, 1.5)
    val out = bilinearInterpolation.interpolate(image)
    assert(out.width == 128 * 1.5)
    assert(out.height == 128 * 1.5)
  }
}
