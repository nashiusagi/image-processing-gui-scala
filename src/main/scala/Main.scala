import scalafx.application.JFXApp3
import scalafx.scene.Scene
import img.Image
import scalafx.scene.canvas.Canvas
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import scala.util.{Success, Failure}
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import img._
import view.RootView

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    val bufImage: BufferedImage = img.load("./resources/sample.jpg") match {
      case Success(src) => src
      case Failure(_)   => new BufferedImage(128, 128, TYPE_INT_ARGB)
    }
    val origPixels: Seq[Pixel] = img.bufferedImageToPixels(bufImage)
    val image: Image =
      new Image(origPixels, bufImage.getWidth, bufImage.getHeight)

    val writableImage = new WritableImage(image.width, image.height)
    SwingFXUtils.toFXImage(image.image, writableImage)

    val canvas = new Canvas(128, 128);
    val gc = canvas.graphicsContext2D
    gc.drawImage(writableImage, 0, 0)

    val canvas2 = new Canvas(128, 128);
    val gc2 = canvas2.graphicsContext2D
    gc2.drawImage(writableImage, 0, 0)

    val root = new RootView(image, canvas, canvas2).getView()

    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(root, 1920, 1080)
    }
  }
}
