package img

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.File
import javax.imageio.ImageIO
import scala.util.Try
import scala.util.{Success, Failure}

class Image(filename: String) {
  val image = load(filename) match {
    case Success(file)      => file
    case Failure(exception) => new BufferedImage(128, 128, TYPE_INT_ARGB)
  }

  val width: Int = image.getWidth()
  val height: Int = image.getHeight()
  val pixels: List[Pixel] = initPixels()

  private def load(filename: String): Try[BufferedImage] = {
    val f: File = new File(filename)

    Try(ImageIO.read(f))
  }

  private def initPixels(): List[Pixel] =
    for (iter <- (0 until width * height).toList) yield {
      val x = iter % width
      val y = iter / height
      val color = image.getRGB(x, y)

      new Pixel(x, y, color)
    }
}
