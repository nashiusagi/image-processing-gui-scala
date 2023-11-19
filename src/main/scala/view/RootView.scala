package view

import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Menu, MenuBar}
import scalafx.scene.layout.{VBox, HBox}

import img._
import view.button._
import view.menu._

class RootView(image: Image, canvas: Canvas, canvas2: Canvas) {
  val gc = canvas.graphicsContext2D
  val gc2 = canvas2.graphicsContext2D

  val gaussianFilterButton = new GaussianFilterButton(image, gc).getView()

  val biliearInterpolateButton: Button =
    new BilinearInterpolateButton(image, gc).getView()

  val saliencyMapButton: Button = new SaliencyMapButton(image, gc).getView()

  val colorSaliencyMapButton: Button =
    new ColorSaliencyMapButton(image, gc).getView()

  val olientSaliencyMapButton: Button =
    new OlientSaliencyMapButton(image, gc).getView()

  val ittiSaliencyMapButton: Button =
    new IttiSaliencyMapButton(image, gc).getView()

  val layer1Button = new LayerButton("layer1", canvas).getView()
  val layer2Button = new LayerButton("layer2", canvas2).getView()
  val layerButtons = new VBox();
  layerButtons.getChildren().addAll(layer1Button, layer2Button)

  val layerPane = new LayerContainer(canvas);
  layerPane.add(canvas2);

  val layers = new HBox();
  layers.getChildren().addAll(layerPane.getView(), layerButtons);

  val fileMenu: Menu = new FileMenu(image, canvas).getView();
  val debugMenu: Menu = new DebugMenu(image, gc).getView();

  val menuBar: MenuBar = new MenuBar()
  menuBar.getMenus().addAll(fileMenu, debugMenu)

  val buttons = new HBox()
  buttons.getChildren.addAll(
    gaussianFilterButton,
    biliearInterpolateButton,
    saliencyMapButton,
    colorSaliencyMapButton,
    olientSaliencyMapButton,
    ittiSaliencyMapButton
  )

  val root = new VBox()
  root.getChildren.addAll(menuBar, buttons, layers)

  def getView() = root;
}
