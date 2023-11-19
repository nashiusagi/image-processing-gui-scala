package view.button

import scalafx.scene.control.Button
import img.Image
import img.saliencyMap.ittiSaliencyMap
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.canvas.GraphicsContext

class ColorSaliencyMapButton(image: Image, gc: GraphicsContext) {
  private val colorSaliencyMapButton = new Button("color saliency map");

  colorSaliencyMapButton.setOnAction((_) -> {
    val saliencyMap = new ittiSaliencyMap();
    val (rg, by) = saliencyMap.calcColorSaliencyMap(image);
    val out = rg.scaling(0.5) + by.scaling(0.5);

    val writableImageFiltered = new WritableImage(out.width, out.height);
    SwingFXUtils.toFXImage(out.image, writableImageFiltered);

    gc.drawImage(writableImageFiltered, 0, 0);
  })

  def getView() = colorSaliencyMapButton;
}
