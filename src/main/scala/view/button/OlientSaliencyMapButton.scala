package view.button

import scalafx.scene.control.Button
import img.Image
import img.saliencyMap.ittiSaliencyMap
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.canvas.GraphicsContext

class OlientSaliencyMapButton(image: Image, gc: GraphicsContext) {
  private val olientSaliencyMapButton = new Button("olient saliency map");

  olientSaliencyMapButton.setOnAction((_) -> {
    val saliencyMap = new ittiSaliencyMap()
    val (map0, map45, map90, map135) =
      saliencyMap.calcOrientationSaliencyMap(image.grayScale())
    val out = map0.scaling(0.25) + map45.scaling(0.25) + map90.scaling(
      0.25
    ) + map135.scaling(0.25)

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  def getView() = olientSaliencyMapButton;
}
