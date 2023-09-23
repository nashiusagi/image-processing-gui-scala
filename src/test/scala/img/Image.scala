package img

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class ImageSpec extends AnyFlatSpec with Diagrams {
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

  "初期化" should "画像が存在しない場合は、ダミーで作成した画像情報を取得しpixelsにピクセル情報を持っている" in {
    assert(image.width === 128)
    assert(image.height === 128)
    assert(image.pixels.length === 128 * 128)
  }
}
