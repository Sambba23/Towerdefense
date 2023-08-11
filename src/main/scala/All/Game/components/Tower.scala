package All.Game.components

import All.Game.Constants
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem}
import scalafx.scene.paint.Color._
import scalafx.scene.paint.Paint
import scalafx.scene.shape._

import scala.collection.mutable.Buffer


class Tower(posX: Double, posY: Double, color: Paint) extends Rectangle{
  var c = color
  var downTime = Constants.downTime
  var shootChecker = false
  var cooldown = 0
  var range = Constants.basicRange
  var w = Constants.towerSide
  width = w
  height = w
  x = posX
  y = posY
  fill = color
}
