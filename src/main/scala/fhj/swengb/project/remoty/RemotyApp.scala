package fhj.swengb.project.remoty


import java.io.{IOException, File}
import java.net.URL
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file._
import java.util.ResourceBundle
import javafx.application.Application
import javafx.collections.{FXCollections, ObservableList}
import javafx.embed.swt.SWTFXUtils
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.{FXML, Initializable, FXMLLoader}
import javafx.scene.control._
import javafx.scene.image.{ImageView, Image}
import javafx.scene.input.{MouseButton, ContextMenuEvent, MouseEvent}
import javafx.scene.layout.{HBox, Pane, StackPane, BorderPane}
import javafx.scene.{Scene, Parent}
import javafx.stage.{DirectoryChooser, Stage}
import java.util
import scala.collection.mutable
import scala.util.{Try, Success, Failure}
import scala.util.control.NonFatal
import scala.collection.JavaConversions._

/**
  * Created by Amar on 19.12.2015.
  */

object RemotyApp {
  def main(args: Array[String]) {
    Application.launch(classOf[RemotyApp], args: _*)
  }
}


class RemotyApp extends javafx.application.Application {


  val Css = "/fhj/swengb/project/remoty/Style.css"
  val Fxml = "/fhj/swengb/project/remoty/Remoty.fxml"
  val Fxml2 = "/fhj/swengb/project/remoty/GUI_1.0.fxml"


  val loader = new FXMLLoader(getClass.getResource(Fxml2))







  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("Remoty")
      loader.load[Parent]() // side effect
      val scene = new Scene(loader.getRoot[Parent]) //loads the default scene
      stage.setScene(scene)
      stage.setResizable(false) //window cannot be rescaled
      val controller1 = loader.getController[RemotyAppController]
      controller1.setStage(stage)
      //stage.getScene.getStylesheets.add(Css)



      stage.show()
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }




}



class RemotyAppController extends Initializable {

  @FXML var pane_view: Pane = _
  @FXML var tree_view: TreeView[PathItem] = _
  //a label to show the actions of the mouseEventHandler
  @FXML var msg_out: Label = _
  @FXML var chooserButton: Button = _
  @FXML var rootLabel: Label = _

  val pictureFolderOpen: Image = new Image("/fhj/swengb/project/remoty/folder-open.png")
  val pictureFolder: Image = new Image("/fhj/swengb/project/remoty/folder.png")
  val pictureFile: Image = new Image("/fhj/swengb/project/remoty/file.png")


  def createNode(pi: PathItem): TreeItem[PathItem] = {
    new TreeItem[PathItem](pi){
      var isLeaf1: Boolean = _
      var isFirstTimeChildren: Boolean = true
      var isFirstTimeLeaf = true


      override def getChildren: ObservableList[TreeItem[PathItem]] = {
        if(isFirstTimeChildren){
          isFirstTimeChildren = false
          super.getChildren.setAll(buildChildren(this))
        }
        super.getChildren
      }


      override def isLeaf: Boolean = {
        if(isFirstTimeLeaf){
          isFirstTimeLeaf = false
          isLeaf1 = Files.isRegularFile(this.getValue.getPath, LinkOption.NOFOLLOW_LINKS)
        }
        isLeaf1
      }

      def buildChildren(treeItem: TreeItem[PathItem]): ObservableList[TreeItem[PathItem]] = {
        val path:Path = treeItem.getValue.getPath

        treeItem.setGraphic((new ImageView(pictureFolderOpen)))

        if(path != null && Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)){
          val children:ObservableList[TreeItem[PathItem]] = FXCollections.observableArrayList()

          val dirs = Files.newDirectoryStream(path).toList//.map(stream => stream.iterator().toIterator.toList.map(path => path.getFileName))).getOrElse(List())


          for(dir <- dirs){
            var child = createNode(new PathItem(dir))
            if(Files.isRegularFile(dir, LinkOption.NOFOLLOW_LINKS)){
              child.setGraphic(new ImageView(pictureFile))
              children.add(child)
            }

            else{
              child.setGraphic(new ImageView(pictureFolder))
              children.add(child)
            }


          }

          return children
        }
        FXCollections.emptyObservableList()
      }


  }
  }


  var stage:Stage = null
  def setStage(s:Stage):Unit ={stage = s}



  override def initialize(location: URL, resources: ResourceBundle): Unit = {
   initializeAll()
  }

  def initializeAll(): Unit = {


    val chooser = new DirectoryChooser
    chooserButton.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        val selected = chooser.showDialog(stage)
        if(selected != null){
          rootLabel.setText(selected.getAbsolutePath)
          val rootPath: String = rootLabel.getText
          val root = Paths.get(rootPath)
          val item = createNode(new PathItem(root))
          item.setExpanded(true)
          item.setGraphic(new ImageView(pictureFolder))
          tree_view.setRoot(item)

          tree_view.setEditable(true)

        }
      }
    })







  }



}





