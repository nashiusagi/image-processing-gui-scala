package view.button

import img.Image
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.control.Button
import scalafx.scene.image.WritableImage
import img.filtering.GaussianFilter
import scalafx.embed.swing.SwingFXUtils

class GaussianFilterButton(image: Image, gc: GraphicsContext) {
  private val gaussianFilterButton: Button = new Button("gassian filter")
  gaussianFilterButton.setOnAction((_) -> {
    val gaussianFilter = new GaussianFilter(3, 1.3)
    val out = gaussianFilter.filtering(image)

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  def getView() = gaussianFilterButton;
}
