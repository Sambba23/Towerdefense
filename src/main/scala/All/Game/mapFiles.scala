package All.Game

import All.Game.fileHandler.{Line, fileName}
import upickle.legacy.writer

import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.Buffer
import java.io._
import scala.Vector
import scala.io.Source
import scala.reflect.internal.util.NoPosition.line
import scala.reflect.io.File.separator
import scala.reflect.runtime.universe.If
import scala.util.Try

object mapFiles {

  val targetFile = "SaveMap.txt"
  //saves the new map
  def writeVectorToFile(vector: Vector[(Int, Int)], mapName: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(targetFile, true))
    writer.write("###" + mapName +"\n")
    for ((key, value) <- vector) {
      writer.write(s"$key,$value\n")
    }
    writer.write("-----\n")
    println("successfully writed a new map")
    writer.close()
  }

  //reads old maps
  def readVectorFromFile(name: String): Vector[(Int, Int)] = {
    val source = Source.fromFile(targetFile)
    try {
      val lines = source.getLines().toList
      val index = lines.indexOf("###" + name)
      if (index == -1) {
        throw new Exception(s"Map '$name' not found")
      }
      val end = lines.indexOf("-----", index)
      lines.slice(index + 1, end).map { line =>
        val Array(key, value) = line.split(",")
        (key.toInt, value.toInt)
      }.toVector
    } finally {
      source.close()
    }
}

  //returns all of the maps names from the file
  def mapNames(): String ={
    var helper = Vector[String]()
    var retVal = ""
    val trySource = Try(Source.fromFile(fileName))
    //If there isn't a existing file:
    trySource match {
      case scala.util.Failure(_) =>{
      return "No custom maps Yet :("
      }
      case _ =>{}
    }
    val source = Source.fromFile(targetFile)
    try {
      val lines = source.getLines().toList
      helper = lines.filter(_.startsWith("###")).map(_.substring(3)).toVector
    } finally {
      source.close()
    }
    if (helper.isEmpty) {
      "No custom Maps\nTo make your own map go to Make a map in menu"
    } else {
      helper.foreach(x => retVal += x + "\n")
      retVal
    }
  }
}
