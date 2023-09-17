import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import img.Image

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    val image: Image = new Image("./resources/sample.png")
    println(image.pixels(24 * 50 + 24).getColor())
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene {
        root = new BorderPane {
          padding = Insets(25)
          center = new Label("Hello SBT")
        }
      }
    }
  }
}
