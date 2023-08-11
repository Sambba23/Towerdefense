package All.Game.components

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.Paint
import scalafx.scene.shape._

class GhostTower(posX: Double, posY: Double, w: Int, color: Paint ) extends Rectangle {
  var c = color
  width = w
  height = w
  x = posX
  y = posY
  fill = c
}
