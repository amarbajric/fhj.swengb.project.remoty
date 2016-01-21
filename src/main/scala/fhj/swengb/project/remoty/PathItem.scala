package fhj.swengb.project.remoty

import java.io.File
import java.nio.file.Path
import javafx.scene.image.ImageView


/**
  * Created by chris on 19.01.2016.
  *
  */

object PathItem{
  def apply(path:Path):PathItem = PathItem(path)
}

class PathItem (path:Path)  {

def getPath:Path= {
      path
  }

  override def toString:String = {
    if(path.getFileName == null)
       path.toString
    else
       path.getFileName.toString //show the filename, not the path in the TreeView
  }

}
