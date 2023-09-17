import scalafx.application.JFXApp3
import scalafx.scene.Scene
import img.Image
import scalafx.scene.layout.HBox
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.Pane
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    val image: Image = new Image("./resources/sample.jpg")
    val writableImage = new WritableImage(image.width, image.height)
    SwingFXUtils.toFXImage(image.image, writableImage)

    val canvas = new Canvas(image.width, image.height);
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
}
