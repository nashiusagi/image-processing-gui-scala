package view.button

import scalafx.scene.control.Button
import img.Image
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.canvas.GraphicsContext
import img.BilinearInterpolation

class BilinearInterpolateButton(image: Image, gc: GraphicsContext) {
  private val bilinearInterpolateButton = new Button("bi-linear");

  bilinearInterpolateButton.setOnAction((_) -> {
    val bilinearInterpolation = new BilinearInterpolation(1.5, 1.5)
    val out = bilinearInterpolation.interpolate(image)

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  def getView() = bilinearInterpolateButton;
}
