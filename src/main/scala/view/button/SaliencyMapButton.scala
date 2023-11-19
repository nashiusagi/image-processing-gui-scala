package view.button

import scalafx.scene.canvas.GraphicsContext
import img.Image
import scalafx.scene.control.Button
import img.saliencyMap.SaliencyMap
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils

class SaliencyMapButton(image: Image, gc: GraphicsContext) {
  private val saliencyMapButton = new Button("saliency map");

  saliencyMapButton.setOnAction((_) -> {
    val saliencyMap = new SaliencyMap()
    val out = saliencyMap.saliencyMap(image.grayScale())

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  def getView() = saliencyMapButton;
}
