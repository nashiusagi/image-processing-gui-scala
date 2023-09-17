package img

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams

class ImageSpec extends AnyFlatSpec with Diagrams{
  val image = new Image("./resources/sample.jpg")

  "初期化" should "画像情報を取得しpixelsにピクセル情報を持っている" in {
    assert(image.width === 400)
    assert(image.height === 400)
    assert(image.pixels.length === 160000)
  }
}
