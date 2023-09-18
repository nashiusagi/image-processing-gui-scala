import scalafx.application.JFXApp3
import scalafx.scene.Scene
import img.Image
import scalafx.scene.layout.HBox
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.Pane
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import img.filtering.GaussianFilter
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.File
import javax.imageio.ImageIO
import img.Pixel

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    val bufImage: BufferedImage = load("./resources/sample.png") match {
      case Success(src) => src
      case Failure(_)   => new BufferedImage(128, 128, TYPE_INT_ARGB)
    }
    val origPixels: List[Pixel] = bufferedImage2Pixels(bufImage)
    val image: Image =
      new Image(origPixels, bufImage.getWidth, bufImage.getHeight)
    // val writableImage = new WritableImage(image.width, image.height)
    // SwingFXUtils.toFXImage(image.image, writableImage)

    //
    val gaussianFilter = new GaussianFilter(3, 1.3)
    val out = gaussianFilter.filtering(image)
    val writableImage = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImage)

    val canvas = new Canvas(out.width, out.height);
    val gc = canvas.graphicsContext2D
    gc.drawImage(writableImage, 0, 0)

    val layerPane = new Pane();
    layerPane.getChildren().addAll(canvas)
    val root = new HBox()
    root.getChildren.addAll(layerPane)

    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(root)
    }
  }

  def load(filename: String): Try[BufferedImage] = {
    val file: File = new File(filename)

    Try(ImageIO.read(file))
  }

  def bufferedImage2Pixels(src: BufferedImage): List[Pixel] = {
    val width: Int = src.getWidth
    val height: Int = src.getHeight

    for (iter <- (0 until width * height).toList) yield {
      val x = iter % width
      val y = iter / height
      val color = src.getRGB(x, y)

      new Pixel(x, y, color)
    }
  }
}
