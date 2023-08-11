package All.Game.components

import All.Game.Constants
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.shape._

import scala.collection.mutable.Buffer

class Balloon(speed: Double, color: Color) extends Circle {
  var mSpeed = speed
  var position = 0
  var c = color
  var r = Constants.ballRadius
  centerX = Constants.balloonStartX
  centerY = Constants.balloonStartY
  fill = c
  radius = r
}