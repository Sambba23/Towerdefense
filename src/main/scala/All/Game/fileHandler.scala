package All.Game

import java.io.{BufferedWriter, FileWriter}
import scala.collection.mutable.{ArrayBuffer, Buffer}
import scala.io.Source
import java.nio.file.{Files, Paths}
import scala.reflect.internal.util.NoPosition.source
import scala.util.Try

object fileHandler {
  val fileName = "Results.txt"
  //writes new result
  def writeToTextFile(name: String, round: Int, g: Game): Unit = {
    val writer = new BufferedWriter(new FileWriter(fileName, true))
    if (!g.modified){
    writer.write("\n" + name + " ROUND: " + round)
    writer.close()
    println(s"Successfully wrote to file $fileName")
    } else {
      println("Map is modified, therefore it will not be written to file")
    }
  }

  //helper for the function readTextFile
  case class Line(name: String, round: Int)
  //Reads the results
  def readTextFile(): String = {
    //helps with sorting the top scores
    var buffer = new ArrayBuffer[Line]

    var fileContents = ""

    //Is there a file existing?
    val trySource = Try(Source.fromFile(fileName))
    //If there isn't a existing file:
    trySource match {
      case scala.util.Failure(_) =>{
      return "No results yet :(\nPlay a game in the normal map to save your first result!"
      }
      case _ =>{}
    }

    //If there is a file existing:
    val source = Source.fromFile(fileName)

    try {
      for (line <- source.getLines) {
        val parts = line.split("ROUND: ")
        if (parts.length == 2) {
          val name = parts(0).trim
          val round = parts(1).trim.toInt
          buffer += Line(name, round)
        }
      }
    } finally {
      source.close()
    }
    //puts contents of the buffer in the right order and then returns updated filecontents
    buffer.sortBy(_.round).reverse.foreach( x => fileContents += x.name.take(6) + " RESULT: " + x.round + "\n")
    if (fileContents == "") {
      "No results yet :(\nPlay a game in the normal map to save your first result!"
    } else {
      fileContents
    }
  }



}
