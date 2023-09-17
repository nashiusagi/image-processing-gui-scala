package img

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.diagrams.Diagrams
import scalafx.scene.paint.Color

class PixelSpec extends AnyFlatSpec with Diagrams {
  val redPixel = new Pixel(0, 0, 0xffff0000)
  val greenPixel = new Pixel(0, 0, 0xff00ff00)
  val bluePixel = new Pixel(0, 0, 0xff0000ff)

  "red pixel" should "赤色" in {
    assert(redPixel.getColor() === Color.Red)
    assert(redPixel.getColor() !== Color.Green)
    assert(redPixel.getColor() !== Color.Blue)
  }

  "green pixel" should "緑色" in {
    assert(greenPixel.getColor() !== Color.Red)
    assert(greenPixel.getColor() === Color.Lime)
    assert(greenPixel.getColor() !== Color.Blue)
  }

  "blue pixel" should "青色" in {
    assert(bluePixel.getColor() !== Color.Red)
    assert(bluePixel.getColor() !== Color.Green)
    assert(bluePixel.getColor() === Color.Blue)
  }
}
