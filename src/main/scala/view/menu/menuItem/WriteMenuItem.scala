package view.menu.menuItem

import img.Image
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.MenuItem
import scalafx.scene.image.WritableImage
import scalafx.scene.SnapshotParameters
import scalafx.embed.swing.SwingFXUtils
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB

class WriteMenuItem(image: Image, canvas: Canvas) {
  private val writeMenu: MenuItem = new MenuItem("save canvas as png")
  writeMenu.setOnAction((_) -> {
    val imagews = new WritableImage(image.width, image.height)
    val imagew = canvas.snapshot(new SnapshotParameters(), imagews)
    val file: File = new File("resources/out.png")
    val buf = new BufferedImage(image.width, image.height, TYPE_INT_ARGB)
    ImageIO.write(SwingFXUtils.fromFXImage(imagew, buf), "png", file)
  })

  def getView() = writeMenu;
}
