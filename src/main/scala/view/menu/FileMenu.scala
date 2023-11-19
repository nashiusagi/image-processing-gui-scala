package view.menu

import scalafx.scene.control.Menu
import view.menu.menuItem.WriteMenuItem
import img.Image
import scalafx.scene.canvas.Canvas

class FileMenu(image: Image, canvas: Canvas) {
  private val fileMenu: Menu = new Menu("_File")

  private val writeMenuItem = new WriteMenuItem(image, canvas).getView()
  fileMenu.getItems.addAll(writeMenuItem)

  def getView() = fileMenu
}
