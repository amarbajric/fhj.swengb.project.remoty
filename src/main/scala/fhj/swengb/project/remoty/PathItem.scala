package fhj.swengb.project.remoty

import java.nio.file.Path

/**
  * Created by chris on 19.01.2016.
  */
class PathItem (path:Path) {

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