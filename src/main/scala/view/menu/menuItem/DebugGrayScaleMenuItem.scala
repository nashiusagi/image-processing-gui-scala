package view.menu.menuItem

import img.Image
import scalafx.scene.canvas.GraphicsContext
import scalafx.scene.control.MenuItem
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils

class DebugGrayScaleMenuItem(image: Image, gc: GraphicsContext) {
  private val debugGrayScaleMenuItem: MenuItem = new MenuItem(
    "grayscale"
  );

  debugGrayScaleMenuItem.setOnAction((_) -> {
    val out = image.grayScale()

    val writableImageFiltered = new WritableImage(out.width, out.height)
    SwingFXUtils.toFXImage(out.image, writableImageFiltered)
    gc.drawImage(writableImageFiltered, 0, 0)
  })

  def getView() = debugGrayScaleMenuItem;
}
