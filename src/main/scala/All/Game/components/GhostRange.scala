package All.Game.components
import All.Game.Constants
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem}
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color._
import scalafx.scene.shape._


class GhostRange(x: Double, y: Double) extends Circle {
  centerX = x
  centerY = y
  fill = White
  radius = Constants.basicRange
  opacity = 0.5
}
