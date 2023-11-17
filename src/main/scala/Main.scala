import img.saliencyMap.SaliencyMap
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import img.Image
import scalafx.scene.layout.HBox
import scalafx.scene.canvas.Canvas
import scalafx.scene.layout.Pane
import scalafx.scene.image.WritableImage
import scalafx.embed.swing.SwingFXUtils
import img.filtering.GaussianFilter
import scala.util.Success
import scala.util.Failure
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import img.Pixel
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox
import img._
import _root_.img.saliencyMap.ittiSaliencyMap
import _root_.img.filtering.GaborFilter
import scalafx.scene.SnapshotParameters
import java.io.File
import javax.imageio.ImageIO
import scalafx.stage.Stage
import scalafx.scene.control.Slider
import scalafx.scene.control.Label
import scalafx.scene.control.MenuItem
import scalafx.scene.control.Menu
import scalafx.scene.control.MenuBar

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    val bufImage: BufferedImage = img.load("./resources/sample.jpg") match {
      case Success(src) => src
      case Failure(_)   => new BufferedImage(128, 128, TYPE_INT_ARGB)
    }
    val origPixels: Seq[Pixel] = img.bufferedImageToPixels(bufImage)
    val image: Image =
      new Image(origPixels, bufImage.getWidth, bufImage.getHeight)

    val writableImage = new WritableImage(image.width, image.height)
    SwingFXUtils.toFXImage(image.image, writableImage)

    val canvas = new Canvas(128, 128);
    val gc = canvas.graphicsContext2D
    gc.drawImage(writableImage, 0, 0)

    val canvas2 = new Canvas(128, 128);
    val gc2 = canvas2.graphicsContext2D
    gc2.drawImage(writableImage, 0, 0)

    val gaussianFilterButton: Button = new Button("gassian filter")
    gaussianFilterButton.setOnAction((_) -> {
      val gaussianFilter = new GaussianFilter(3, 1.3)
      val out = gaussianFilter.filtering(image)

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val biliearInterpolateButton: Button = new Button("bi-linear")
    biliearInterpolateButton.setOnAction((_) -> {
      val bilinearInterpolation = new BilinearInterpolation(1.5, 1.5)
      val out = bilinearInterpolation.interpolate(image)

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val saliencyMapButton: Button = new Button("saliency map")
    saliencyMapButton.setOnAction((_) -> {
      val saliencyMap = new SaliencyMap()
      val out = saliencyMap.saliencyMap(image.grayScale())

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val colorSaliencyMapButton: Button = new Button("color saliency map")
    colorSaliencyMapButton.setOnAction((_) -> {
      val saliencyMap = new ittiSaliencyMap()
      val (rg, by) = saliencyMap.calcColorSaliencyMap(image)
      val out = rg.scaling(0.5) + by.scaling(0.5)

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val olientSaliencyMapButton: Button = new Button("olient saliency map")
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

    val ittiSaliencyMapButton: Button = new Button("itti saliency map")
    ittiSaliencyMapButton.setOnAction((_) -> {
      val saliencyMap = new ittiSaliencyMap()
      val out =
        saliencyMap.saliencyMap(image).grayScale().normalize().grayScale2Jet()

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val writeMenu: MenuItem = new MenuItem("save canvas as png")
    writeMenu.setOnAction((_) -> {
      val imagews = new WritableImage(image.width, image.height)
      val imagew = canvas.snapshot(new SnapshotParameters(), imagews)
      val file: File = new File("resources/out.png")
      val buf = new BufferedImage(image.width, image.height, TYPE_INT_ARGB)
      ImageIO.write(SwingFXUtils.fromFXImage(imagew, buf), "png", file)
    })

    val debugGrayScaleMenu: MenuItem = new MenuItem("grayscale")
    debugGrayScaleMenu.setOnAction((_) -> {
      val out = image.grayScale()

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val debugGaborKernelMenu: MenuItem = new MenuItem("gabor filtering")
    debugGaborKernelMenu.setOnAction((_) -> {
      val angleSliderWindow: Stage = new Stage()

      val angleSlider: Slider = new Slider()
      angleSlider.setMin(0.0)
      angleSlider.setMax(180.0)
      angleSlider.setMajorTickUnit(45.0)
      angleSlider.setMinorTickCount(3)
      angleSlider.setShowTickLabels(true)
      angleSlider.setShowTickMarks(true)
      angleSlider.setPrefWidth(200)

      val gaborKernelCanvas = new Canvas(111, 111);

      val lblSliderValue: Label =
        new Label("gabor angle: " + angleSlider.getValue().toString + " [deg]")
      lblSliderValue.setPrefWidth(200);

      angleSlider.valueProperty.addListener {
        (
            o: javafx.beans.value.ObservableValue[_ <: Number],
            oldVal: Number,
            newVal: Number
        ) =>
          angleSlider.setValue(newVal.intValue())
          lblSliderValue.setText(
            "gabor angle: " + newVal.intValue().toString() + " [deg]"
          )
      }

      val applyGaborButton: Button = new Button("show kernel preview")
      applyGaborButton.setOnAction((_) -> {
        val angle = angleSlider.getValue()

        val gaborFilter = new GaborFilter(111, 10.0, 1.2, 10, 0, angle.toInt)
        val out = gaborFilter.kernelToImage

        val writableImageFiltered = new WritableImage(out.width, out.height)
        SwingFXUtils.toFXImage(out.image, writableImageFiltered)
        val gaborKernelGC = gaborKernelCanvas.graphicsContext2D
        gaborKernelGC.drawImage(writableImageFiltered, 0, 0)
      })

      val gaborFilteringButton: Button = new Button("filtering!")
      gaborFilteringButton.setOnAction((_) -> {
        val angle = angleSlider.getValue()

        angleSliderWindow.close()

        val gaborFilter = new GaborFilter(11, 1.5, 1.2, 3, 0, angle.toInt)
        val out = gaborFilter.filtering(image.grayScale()).normalize()

        val writableImageFiltered = new WritableImage(out.width, out.height)
        SwingFXUtils.toFXImage(out.image, writableImageFiltered)
        gc.drawImage(writableImageFiltered, 0, 0)
      })

      val sliderRoot = new VBox()
      sliderRoot.getChildren
        .addAll(
          angleSlider,
          lblSliderValue,
          applyGaborButton,
          gaborKernelCanvas,
          gaborFilteringButton
        )

      angleSliderWindow.setTitle("sample")
      val anglePane: Pane = new Pane()
      anglePane.setPrefSize(500, 250)
      anglePane.getChildren().addAll(sliderRoot)

      // angleSliderWindow.setScene(new Scene(200, 100))
      angleSliderWindow.setScene(new Scene(anglePane))
      angleSliderWindow.show()
    })

    val layer1Button: Button = new Button("layer1");
    layer1Button.setOnAction((_)->{
      canvas.toFront();
    });
    val layer2Button: Button = new Button("layer2");
    layer2Button.setOnAction((_)->{
      canvas2.toFront();
    });

    val layerPane = new Pane();
    layerPane.getChildren().addAll(canvas,canvas2)

    val layerButtons = new VBox();
    layerButtons.getChildren().addAll(layer1Button, layer2Button)

    val layers = new HBox();
    layers.getChildren().addAll(layerPane, layerButtons);

    val fileMenu: Menu = new Menu("_File")
    fileMenu.getItems().addAll(writeMenu)

    val debugMenu: Menu = new Menu("_Debug")
    debugMenu.getItems().addAll(debugGrayScaleMenu, debugGaborKernelMenu)

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

    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(root)
    }
  }
}
