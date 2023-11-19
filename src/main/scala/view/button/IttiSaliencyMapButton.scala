package view.button

import scalafx.scene.control.Button
import img.Image
import img.saliencyMap.ittiSaliencyMap
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.canvas.GraphicsContext

class IttiSaliencyMapButton(image: Image, gc: GraphicsContext) {
  private val ittiSaliencyMapButton = new Button("Itti saliency map");

  ittiSaliencyMapButton.setOnAction((_) -> {
    val saliencyMap = new ittiSaliencyMap()
    val out =
      saliencyMap.saliencyMap(image).grayScale().normalize().grayScale2Jet()

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  def getView() = ittiSaliencyMapButton;
}
