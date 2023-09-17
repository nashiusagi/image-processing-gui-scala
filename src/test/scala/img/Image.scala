package img

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams

class ImageSpec extends AnyFlatSpec with Diagrams {
  val image = new Image("nodata.jpg")

  "初期化" should "画像が存在しない場合は、ダミーで作成した画像情報を取得しpixelsにピクセル情報を持っている" in {
    assert(image.width === 128)
    assert(image.height === 128)
    assert(image.pixels.length === 128 * 128)
  }
}
