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

object HelloSBT extends JFXApp3 {
  override def start(): Unit = {
    val bufImage: BufferedImage = img.load("./resources/sample.jpg") match {
      case Success(src) => src
      case Failure(_)   => new BufferedImage(128, 128, TYPE_INT_ARGB)
    }
    val origPixels: List[Pixel] = img.bufferedImageToPixels(bufImage)
    val image: Image =
      new Image(origPixels, bufImage.getWidth, bufImage.getHeight)

    val writableImage = new WritableImage(image.width, image.height)
    SwingFXUtils.toFXImage(image.image, writableImage)

    val canvas = new Canvas(128, 128);
    val gc = canvas.graphicsContext2D
    gc.drawImage(writableImage, 0, 0)

    val grayscaleButton: Button = new Button("grayscale")
    grayscaleButton.setOnAction((_) -> {
      val out = image.grayScale()

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

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

    val gaborButton: Button = new Button("gabor")
    gaborButton.setOnAction((_) -> {
      val angleSliderWindow: Stage = new Stage()

      val angleSlider: Slider = new Slider()
      angleSlider.setMin(0.0)
      angleSlider.setMax(180.0)
      angleSlider.setMajorTickUnit(45.0)
      angleSlider.setMinorTickCount(3)
      angleSlider.setShowTickLabels(true)
      angleSlider.setShowTickMarks(true)
      angleSlider.setPrefWidth(200)

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

      val applyGaborButton: Button = new Button("apply")
      applyGaborButton.setOnAction((_) -> {
        val angle = angleSlider.getValue()

        val gaborFilter = new GaborFilter(111, 10.0, 1.2, 10, 0, angle.toInt)
        val out = gaborFilter.kernelToImage

        // close the window
        angleSliderWindow.close()

        val writableImageFiltered = new WritableImage(out.width, out.height)
        SwingFXUtils.toFXImage(out.image, writableImageFiltered)
        gc.drawImage(writableImageFiltered, 0, 0)
      })

      val sliderRoot = new VBox()
      sliderRoot.getChildren
        .addAll(angleSlider, lblSliderValue, applyGaborButton)

      angleSliderWindow.setTitle("sample")
      val anglePane: Pane = new Pane()
      anglePane.setPrefSize(300, 150)
      anglePane.getChildren().addAll(sliderRoot)

      // angleSliderWindow.setScene(new Scene(200, 100))
      angleSliderWindow.setScene(new Scene(anglePane))
      angleSliderWindow.show()
    })

    val gaborFilterButton: Button = new Button("gabor filter")
    gaborFilterButton.setOnAction((_) -> {
      val gaborFilter = new GaborFilter(11, 1.5, 1.2, 3, 0, 0)
      val out = gaborFilter.filtering(image.grayScale()).normalize()

      val writableImageFiltered = new WritableImage(out.width, out.height)
      SwingFXUtils.toFXImage(out.image, writableImageFiltered)
      gc.drawImage(writableImageFiltered, 0, 0)
    })

    val writeButton: Button = new Button("save canvas")
    writeButton.setOnAction((_) -> {
      val imagews = new WritableImage(image.width, image.height)
      val imagew = canvas.snapshot(new SnapshotParameters(), imagews)
      val file: File = new File("resources/out.png")
      val buf = new BufferedImage(image.width, image.height, TYPE_INT_ARGB)
      ImageIO.write(SwingFXUtils.fromFXImage(imagew, buf), "png", file)
    })

    val layerPane = new Pane();
    layerPane.getChildren().addAll(canvas)
    val buttons = new HBox()
    buttons.getChildren.addAll(
      grayscaleButton,
      gaussianFilterButton,
      biliearInterpolateButton,
      saliencyMapButton,
      colorSaliencyMapButton,
      olientSaliencyMapButton,
      gaborButton,
      ittiSaliencyMapButton,
      gaborFilterButton,
      writeButton
    )
    val layer = new HBox()
    layer.getChildren.addAll(canvas)
    val root = new VBox()
    root.getChildren.addAll(buttons, layer)

    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(root)
    }
  }
}
