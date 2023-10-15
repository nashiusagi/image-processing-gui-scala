package img

import scala.util.Try
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

package object img {
  def load(filename: String): Try[BufferedImage] = {
    val file: File = new File(filename)

    Try(ImageIO.read(file))
  }

  def bufferedImageToPixels(src: BufferedImage): List[Pixel] = {
    val width: Int = src.getWidth
    val height: Int = src.getHeight

    for (iter <- (0 until width * height).toList) yield {
      val x = iter % width
      val y = iter / width
      val color = src.getRGB(x, y)

      new Pixel(x, y, color)
    }
  }
}
