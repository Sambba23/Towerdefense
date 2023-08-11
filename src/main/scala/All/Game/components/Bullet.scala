package All.Game.components

import All.Game.Constants
import All.Game.components.{Balloon, Tower}
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem}
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape._

class Bullet(towards: Balloon, from: Tower) extends Circle {
  val targetBalloon = towards
  var c = from.c
  var r = Constants.bullRadius
  var speed = 30
  centerX = (from.x + from.width/2).toDouble
  centerY = (from.y + from.width/2).toDouble
  fill = c
  radius = 5.0
}
