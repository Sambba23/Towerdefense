package All.Game

import All.Game.components.{Balloon, Bullet, Tower}
import javafx.stage.Stage
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Menu, MenuBar, MenuItem}
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape._

import scala.collection.mutable.Buffer


class Game {
  var upgradePrice = 800
  var round = 0
  var money = 500
  var health = 100
  var hitCounter = 0
  //path that the balloons follow
  var path = Buffer[(Int, Int)]((100, 250), (100, 75), (350, 75), (350, 500), (550, 500), ((Constants.windowSide + Constants.ballRadius*4).toInt, 500))
  //Is map the normal or not?
  var modified = false
  //All balloons that are in the game
  var balloons = Buffer[Balloon]()
  var towers = Buffer[Tower]()
  var bullets = Buffer[Bullet]()
  //delete these balloons after every tick. Avoids errors in playround() function in MainFX
  var deleteBullets = Buffer[Bullet]()
  var deleteBalloons = Buffer[Balloon]()

  def emptyPath() = {
    path.clear()
  }
  //creates a new path
  def addNewPath(points: Buffer[(Int, Int)]): Unit ={
    path = points
  }
  //Adds a new baloon to the game
  def addBalloon(balloon: Balloon): Unit ={
    balloons += balloon
  }
  def addTower(tower: Tower): Unit ={
    towers += tower
  }
  def addBullet(bullet: Bullet) ={
    bullets += bullet
  }
  def deleteBullet(bullet: Bullet): Unit ={
    deleteBullets += bullet
  }
  def deleteBalloon(balloon: Balloon): Unit ={
    deleteBalloons += balloon
  }
}
