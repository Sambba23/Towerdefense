package All.Game

import All.Game.components.{Balloon, Bullet, GhostPoint, GhostRange, GhostTower, Tower}
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Node.sfxNode2jfx
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, Menu, MenuBar, MenuItem, TextField}
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape._
import scalafx.scene.image._
import scalafx.scene.input.MouseEvent
import scalafx.scene.input._
import scalafx.scene.layout.{Background, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle.sfxCircle2jfx
import scalafx.stage.{PopupWindow, Stage}

import scala.util.Random._
import scala.math._
import java.io.{FileInputStream, InputStream}
import scala.collection.mutable.Buffer
import scala.reflect.internal.util.NoSourceFile.content
import scala.util.Random


object Main extends JFXApp3{
  def start(): Unit = {

    //creates a new primary stage
    stage = new JFXApp3.PrimaryStage{
      title = "Cloon TD Battles"
      width = Constants.windowSide
      height = Constants.windowSide

      //makes a new Game object
      var gameObject = new Game

      val takeColor = Buffer(Blue, Yellow, Red, Green)
      var colorCounter = 2
      //Color picker
      var colorB = takeColor(colorCounter)


      //Is there a game going on
      var gameGoing = false
      //Is there a round going
      var roundGoing = false

      //menubar here
      val menuBar = new MenuBar

      //GAME CONTENTS
      var gameContents =  Buffer(menuBar)

      //Basic map

      //Main menu here
      val mainMenu = new Menu("Menu")
      val startItem = new MenuItem("New game")
      val backMain = new MenuItem("Main menu")
      val leaders = new MenuItem("Leaderboards")
      val newMap = new MenuItem("Make a map")
      val customMaps = new MenuItem("Custom maps")
      val exitItem = new MenuItem("Exit")
      val startBut = new Button("Start")
      val newGameBut = new Button("New game")
      mainMenu.items = List(backMain, startItem, newMap, customMaps, leaders, exitItem)

      //play Menu here
      val startButton = new Button("To Start the game\nPress Here")
      val colorrec = new GhostTower(50, 50, 30, colorB)
      var towButton = new Button("Add Tower\n50$", colorrec)
      var showRound = new Label(s"Round: ${gameObject.round}")
      var showMoney = new Label(s"Money: ${gameObject.money}")
      var showHealth = new Label(s"Health: ${gameObject.health}")
      var colorButton = new Button("Change color")
      var upgradeButton = new Button("Upgrade all existing towers\n800$")
      val message = new Label("Welcome to the clone td battles")



      //Adds different menus to the menubar
      menuBar.menus = List(mainMenu)

      var displayableScene = newScene(menuBar, 700, 700, false)

      //creates a new playground for us
      scene = displayableScene
      displayableScene.fill = Grey
      displayableScene.content = Buffer(startBut, message)
      message.textFill = White
      message.relocate(Constants.windowSide/2 - 100, Constants.windowSide/2 - 100)
      startBut.relocate(Constants.windowSide/2 - 50, Constants.windowSide/2 - 50)

      //helps with creating a new scene
      def newScene(menuBar: MenuBar, width: Double, height: Double, game: Boolean): Scene = {
        // new scene is the main menu if game==false
        // otherwise it's the game scene
        displayableScene = new Scene(width, height){

          if (game) {
            upgradeButton.text = s"Upgrade all existing towers\n${gameObject.upgradePrice}" + "$"
            content = Buffer(menuBar, towButton, colorButton, showRound, showMoney, showHealth, startButton, upgradeButton)
            towButton.relocate(550, 0)
            colorButton.relocate(450, 0)
            showRound.relocate(600, 600)
            showMoney.relocate(600, 610)
            showHealth.relocate(600, 620)
            startButton.relocate(Constants.windowSide/2-50, 0)
            startButton.visible = true
            colorButton.textFill = colorB
            upgradeButton.relocate(50, 600)
            fill = Grey
            //Draws the first line of the Path to the screen
            content += new Line{
              startX = gameObject.path(0)._1 - 1000
              startY = gameObject.path(0)._2
              endX = gameObject.path(0)._1
              endY = gameObject.path(0)._2
              strokeWidth = Constants.lineWidth
            }
            //Draws rest of the lines of the path to the screen
            for (x <- 0 until (gameObject.path.length - 1)) {
              if (x != gameObject.path.length - 1) {
                  if (x % 2 == 0) {
                    content += new Line{
                      startX = gameObject.path(x)._1
                      startY = gameObject.path(x)._2
                      endX = gameObject.path(x + 1)._1
                      endY = gameObject.path(x + 1)._2
                      strokeWidth = Constants.lineWidth
                    }
                  } else {
                    content += new Line{
                      startX = gameObject.path(x)._1
                      startY = gameObject.path(x)._2
                      endX = gameObject.path(x + 1)._1
                      endY = gameObject.path(x + 1)._2
                      strokeWidth = Constants.lineWidth
                  }
              }
            }
          }
          content += new Line{
            startX = gameObject.path.last._1
            startY = gameObject.path.last._2
            endX = gameObject.path.last._2 + 10000
            endY = gameObject.path.last._2
            strokeWidth = Constants.lineWidth

          }

          if (!gameObject.modified){
            Constants.balloonStartY = gameObject.path.head._2
          }
          } else {
            content = Buffer(menuBar)
          }
        }

      //put all the important items in front:
      menuBar.toFront()
      towButton.toFront()
      colorButton.toFront()
      showMoney.toFront()
      showHealth.toFront()
      startButton.toFront()
      upgradeButton.toFront()
      showRound.toFront()
      //Change some colors:
      showHealth.textFill = White
      showRound.textFill = White
      showMoney.textFill = White
      displayableScene
      //ends newScene
      }

      //Reload maps from file
      def customMapsscreen()={
        // First set up the Scene:
        gameGoing = false
        tick.stop()
        var newPoints = Buffer[(Int, Int)]()
        gameObject = new Game
        gameObject.modified = true
        gameObject.emptyPath()
        displayableScene = newScene(menuBar, 700, 700, false)
        scene = displayableScene
        displayableScene.fill = Grey


        //Then all of the items that are only in this screen:
        val names = new Label(mapFiles.mapNames())
        val infoText = new Label("To play a custom map type the name of the map here and press Enter\nTo make a new map go to Make a Map in menu!")
        val allMaps = new Label("Names of your custom maps:")
        allMaps.textFill = White
        infoText.textFill = White
        val inputName = new TextField
        val popUP = new VBox(infoText, inputName)
        displayableScene.content = Buffer(menuBar, names, popUP, allMaps)
        names.relocate(20, 60)
        popUP.relocate(300, 50)
        allMaps.relocate(20, 40)

        inputName.onAction = _ => {
          newPoints = mapFiles.readVectorFromFile(inputName.text.value).toBuffer
          newPoints ++= Buffer(((Constants.windowSide.toInt + Constants.ballRadius.toInt*4).toInt, newPoints.last._2))
          gameObject.addNewPath(newPoints)
          gameGoing = true
          scene = newScene(menuBar, width = 4000, height = 4000, true)
          displayableScene.content.forEach(_.opacity = 1)
          Constants.balloonStartY = gameObject.path.head._2
        }
      }


      //When calling this you can make your own map!
      def mapMaker(): Unit = {
        //user can give the map a name
        var newMapName = "No Name"
        //sets up the view
        gameGoing = false
        tick.stop()
        var newPoints = Buffer[(Int, Int)]()
        gameObject = new Game
        gameObject.modified = true
        gameObject.emptyPath()
        val inputName = new TextField
        val infoText = new Label("1) Place new points to the map\n2) Type a map name here and press Enter to save the name!\n3) Start playing and your map will be saved")
        val popUP = new VBox(infoText, inputName)
        val newPoint = new Button("New point")
        val readyNow = new Button("Start Game")
        displayableScene = newScene(menuBar, 700, 700, false)
        scene = displayableScene
        displayableScene.fill = Grey
        displayableScene.content = Buffer(menuBar, newPoint, readyNow, popUP)
        readyNow.relocate(100, 30)
        newPoint.relocate(0, 30)
        popUP.relocate(200, 30)
        newPoint.onAction = _ => {
          val poin = new GhostPoint
          poin.opacity = 0.6
          displayableScene.content += poin
          //New points here. Every point is part of the new path. Together they form the new path
          poin.onMouseMoved = x =>{
            poin.relocate((x.getSceneX - poin.radius.toDouble), (x.getSceneY - poin.radius.toDouble))

            poin.onMousePressed = _ => {
              var place = new GhostPoint
              displayableScene.content += place
              place.relocate(x.getSceneX, x.getSceneY)
              newPoints = newPoints ++ Buffer((x.getSceneX.toInt, x.getSceneY.toInt))
              displayableScene.content -= poin
            }
          }
          readyNow.onAction = _ => {
            newPoints ++= Buffer(((Constants.windowSide.toInt + Constants.ballRadius.toInt*4).toInt, newPoints.last._2))
            gameObject.addNewPath(newPoints)
            gameGoing = true
            scene = newScene(menuBar, width = 4000, height = 4000, true)
            displayableScene.content.forEach(_.opacity = 1)
            Constants.balloonStartY = gameObject.path.head._2
            mapFiles.writeVectorToFile(newPoints.toVector, newMapName)
            mapFiles.mapNames()
          }
          inputName.onAction = _ => {
            newMapName = inputName.text.value
            displayableScene.content -= popUP

          }
        }
      }

      //when called the leaderboard will show
      def leaderboard(): Unit = {
        gameGoing = false
        tick.stop()
        displayableScene = newScene(menuBar, 700, 700, false)
        scene = displayableScene
        displayableScene.fill = Grey
        val topScores = new Label(fileHandler.readTextFile())
        displayableScene.content = Buffer(menuBar, topScores)
        topScores.relocate(0, 30)
      }

      def startMenu(): Unit = {
        gameGoing = false
        tick.stop()
        displayableScene = newScene(menuBar, 700, 700, false)
        scene = displayableScene
        displayableScene.fill = Grey
        displayableScene.content = Buffer(startBut, message)
        message.relocate(Constants.windowSide/2 - 100, Constants.windowSide/2 - 100)
        startBut.relocate(Constants.windowSide/2 - 50, Constants.windowSide/2 - 50)
      }

      def levelPicker(): Unit = {
        gameGoing = false
        tick.stop()
        displayableScene = newScene(menuBar, 700, 700, false)
        scene = displayableScene
        displayableScene.fill = Grey
        displayableScene.content = Buffer(menuBar, newGameBut)
        newGameBut.relocate(Constants.windowSide/2 - 30, Constants.windowSide/2 - 30)
      }
      def gameOverScreen(): Unit ={
        val textbo = new Label(s"Game Over!\nYour result: Round ${gameObject.round}\nName:")
        textbo.textFill = Black
        val inputName = new TextField
        val popUP = new VBox(textbo, inputName)
        popUP.relocate(Constants.windowSide/2 -100, Constants.windowSide/2 - 50)
        displayableScene.content += popUP
        //THIS WRITES THE RESULT TO THE Result.txt file
        inputName.onAction = _ =>{
          fileHandler.writeToTextFile(inputName.text.value, gameObject.round, gameObject)
          displayableScene.content -= popUP
          val again = new Button("Play again")
          displayableScene.content += again
          again.relocate(Constants.windowSide/2 -100, Constants.windowSide/2 - 50)
          //STARTS A NEW GAME IF IT IS CALLED
          again.onAction = _ => {
            gameObject = new Game
            gameGoing = true
            gameObject.addNewPath(Buffer((100, 250), (100, 75), (350, 75), (350, 500), (550, 500), ((Constants.windowSide + Constants.ballRadius*4).toInt, 500)))
            scene = newScene(menuBar, width = 4000, height = 4000, true)
            displayableScene.content.forEach(_.opacity = 1)
            displayableScene.content -= again

          }
        }
      }
      // Creates the ticker used in playround()
      var tick = new Ticker(playRound)


      //plays the next round
      def playRound() = {
        val contents = (gameObject.balloons ++ gameObject.bullets ++ gameObject.towers).map(sfxNode2jfx(_))
        displayableScene.content ++= contents.filter(x => !displayableScene.content.contains(x))

        //updates all the parts of the game that change during playing
        gameObject.balloons.foreach(x => moveBalloon(x))
        gameObject.bullets.foreach(x => moveBullet(x))
        gameObject.towers.foreach(x => createBullet(x))
        gameObject.bullets.foreach(x => popBalloons(x))
        gameObject.bullets.foreach(x => removeBullet(x))
        gameObject.deleteBullets.foreach(gameObject.bullets -= _ )
        gameObject.deleteBalloons.foreach(gameObject.balloons -= _)
        gameObject.towers.foreach(x => if (x.cooldown > 0){ x.cooldown -= 1})
        checkHealth()
        updateStats()
        checkRound()
      }

      //updates the round counter and the currency
      def updateStats()={
        //update round
        displayableScene.content -= showRound
        showRound = new Label(s"Round: ${gameObject.round}")
        displayableScene.content += showRound
        showRound.relocate(600, 600)
        //update money
        displayableScene.content -= showMoney
        showMoney = new Label(s"Money: ${gameObject.money}")
        displayableScene.content += showMoney
        showMoney.relocate(600, 610)
        //update health
        displayableScene.content -= showHealth
        showHealth = new Label(s"Health: ${gameObject.health}")
        displayableScene.content += showHealth
        showHealth.relocate(600, 620)
      }

      //pops balloons that have been hitted
      def popBalloons(bullet: Bullet): Unit = {
            if (bullet.targetBalloon.c == bullet.c){
              val dx = bullet.targetBalloon.centerX - bullet.centerX
              val dy = bullet.targetBalloon.centerY - bullet.centerY
              val dist = math.sqrt(dx.toInt*dx.toInt + dy.toInt*dy.toInt)
              if (dist.toDouble <= bullet.targetBalloon.radius.toDouble){
                displayableScene.content -= bullet.targetBalloon
                gameObject.balloons -= bullet.targetBalloon
              }
            }
          }
      //removes the bullets
      def removeBullet(bullet: Bullet): Unit = {
        if (!displayableScene.content.contains(bullet.targetBalloon)) {
          gameObject.deleteBullet(bullet)
          displayableScene.content -= bullet
        }
      }
      //creates bullets
      def createBullet(tower: Tower) ={
        for (x <- Random.shuffle(gameObject.balloons)){
          if ((tower.cooldown == 0) && (tower.c == x.c)) {
            if ((abs(tower.x.toDouble - x.centerX.toDouble) <= tower.range) && (abs(tower.y.toDouble - x.centerY.toDouble) <= tower.range)){
              gameObject.addBullet(new Bullet(x, tower))
              tower.cooldown = tower.downTime
            }
          }
        }
      }
      //function that moves the bullet
      def moveBullet(bullet: Bullet) = {
        val dx = bullet.targetBalloon.centerX - bullet.centerX
        val dy = bullet.targetBalloon.centerY - bullet.centerY
        val dist = math.sqrt(dx.toInt*dx.toInt + dy.toInt*dy.toInt)
        val vx = (dx).toDouble / dist
        val vy = (dy).toDouble / dist
        bullet.centerX = bullet.centerX.toDouble + vx*bullet.speed*0.3
        bullet.centerY = bullet.centerY.toDouble + vy*bullet.speed*0.3
      }

      //move a single balloon
      def moveBalloon(balloon: Balloon) = {
        if (balloon.centerX.toDouble < Constants.windowSide.toDouble){
          var nextPoint = gameObject.path(balloon.position)
          val dx = nextPoint._1.toDouble - balloon.centerX.toDouble
          val dy = nextPoint._2.toDouble - balloon.centerY.toDouble
          val dist = math.sqrt(dx*dx + dy*dy)
          val vx = dx / dist
          val vy = dy / dist
          balloon.centerX = balloon.centerX.toDouble + vx*balloon.mSpeed*0.1
          balloon.centerY = balloon.centerY.toDouble + vy*balloon.mSpeed*0.1
          if ((abs(balloon.centerX.toDouble - nextPoint._1.toDouble) <= 3)  && (abs(balloon.centerY.toDouble - nextPoint._2.toDouble) <= 3)){
            balloon.position += 1
          }
        } else {
          gameObject.health -= 15
          displayableScene.content -= balloon
          gameObject.deleteBalloon(balloon)
          gameObject.deleteBullets ++= gameObject.bullets.filter(_.targetBalloon == balloon)
          gameObject.hitCounter = 20
        }
      }

      //Check if the round has been played. Also creates new balloons for each round
      def checkRound()={
        //if first round
        if (gameObject.round == 0){
          val bal = new Balloon(10, Red)
          gameObject.addBalloon(bal)
          val bal2 = new Balloon(20, Red)
          gameObject.addBalloon(bal2)
          val bal3 = new Balloon(15, Red)
          gameObject.addBalloon(bal3)
          gameObject.round += 1
        } else if (gameObject.balloons.isEmpty){
          gameObject.health += 10
          gameObject.round += 1
          gameObject.money += 300
          //new balls made here
          val rand = new Random
          if (gameObject.round < 6) {
            for (x <- 0 until 2*gameObject.round){
              val balx = new Balloon(rand.between(3, (12 + gameObject.round)), Red)
              gameObject.addBalloon(balx)
            }
          }
          if (gameObject.round >= 2) {
            val balx = new Balloon(rand.between(3, (12 + gameObject.round)), Yellow)
            gameObject.addBalloon(balx)
          }
          if (gameObject.round >= 4) {
            for (x <- 0 until 2*gameObject.round){
              val balx = new Balloon(rand.between(3, (12 + gameObject.round)), takeColor(rand.between(0, 3)))
              gameObject.addBalloon(balx)
          }
        }
      }
      }
      def checkHealth(): Unit ={
        if (gameObject.health <= 0) {
          tick.stop()
          gameGoing = false
          displayableScene.content.forEach(_.opacity = 0.3)
          menuBar.opacity = 1
          gameOverScreen()
        } else if ((gameObject.health <= 50) && (gameObject.health > 20)){
          if (gameObject.hitCounter == 0) {
            displayableScene.fill = Red.opacity(0.3)
          }
        } else if (gameObject.health <= 20){
          if (gameObject.hitCounter == 0) {
            displayableScene.fill = Red.opacity(0.6)
          }
        }
        if (gameObject.hitCounter == 20){
          displayableScene.fill = Red.opacity(0.4)
        }
        if (gameObject.hitCounter != 0){
          gameObject.hitCounter -= 1
        }
        if ((gameObject.hitCounter == 0) && (gameObject.health > 50)){
          displayableScene.fill = Grey
        }
      }


      //All button actions here

      //First menu action buttons

      //Map maker
      newMap.onAction = _ =>{
        mapMaker()
      }
      //Reload maps from file
      customMaps.onAction = _ => {
        customMapsscreen()
      }

      //Exit
      exitItem.onAction = _ => {
        sys.exit(0)
      }
      //start game
      startItem.onAction = _ => {
        gameObject = new Game
        gameGoing = true
        scene = newScene(menuBar, width = 4000, height = 4000, true)
        displayableScene.content.forEach(_.opacity = 1)
      }
      //starts the game. This button appears only when a new game is launched
      startButton.onAction = _ => {
        if (gameGoing){
          tick.start()
        }
        startButton.visible = false
      }
      // test button
      newGameBut.onAction = _ => {
        gameObject = new Game
        tick = new Ticker(playRound)
        gameGoing = true
        scene = newScene(menuBar, width = 4000, height = 4000, true)
      }
      // Main menu button
      backMain.onAction = _ => {
        startMenu()
      }
      startBut.onAction = _ => {
        levelPicker()
      }
      //Shows the leaderboard
      leaders.onAction = _ => {
        leaderboard()
      }
      //Here all the play buttons

      //Adding a new tower:
      // 1. when clicking: creates a new ghost tower 2. howering over the ghost tower 3. clicking when done and then new tower is placed
      towButton.onAction = _ => {
        if (gameObject.money >= 50) {
          var showRange = new GhostRange(Constants.placeGhostX, Constants.placeGhostY)
          var newTow = new GhostTower(Constants.placeGhostX, Constants.placeGhostY, 50 , colorB)
          newTow.opacity = 0.6
          displayableScene.content += newTow
          displayableScene.content += showRange
          showRange.onMouseMoved = x => {
            newTow.relocate((x.getSceneX - 50/2),(x.getSceneY - 50/2))
            showRange.relocate(x.getSceneX - 200, (x.getSceneY - 200) )
            showRange.onMousePressed = _ => {
              displayableScene.content -= newTow
              displayableScene.content -= showRange
              var placeTow = new Tower((x.getSceneX - 50/2),(x.getSceneY - 50/2), colorB)
              gameObject.addTower(placeTow)
              displayableScene.content += placeTow
              gameObject.money -= 50
              displayableScene.content -= showMoney
              showMoney = new Label(s"Money: ${gameObject.money}")
              displayableScene.content += showMoney
              showMoney.relocate(600, 610)
              showMoney.textFill = White
            }
          }
        }
      }

      //Change the color in ColorPicker
      colorButton.onAction = _ => {
        if (colorCounter == 3){
          colorCounter = 0
          colorB = takeColor(colorCounter)
        } else {
          colorCounter += 1
          colorB = takeColor(colorCounter)
        }
        colorrec.c = colorB

        displayableScene.content += new GhostTower(558, 4, 35, colorB)
      }
      //upgrades new towers
      upgradeButton.onAction = _ => {
        if (gameObject.money >= gameObject.upgradePrice) {
          gameObject.towers.foreach(_.range +=50)
          gameObject.towers.foreach(_.downTime -=30)
          gameObject.towers.foreach(x => if (x.downTime < 1){x.downTime = 2})
          gameObject.money -= gameObject.upgradePrice
          displayableScene.content -= showMoney
          showMoney = new Label(s"Money: ${gameObject.money}")
          displayableScene.content += showMoney
          showMoney.relocate(600, 610)
          displayableScene.content -= upgradeButton
          gameObject.upgradePrice += 100
          upgradeButton.text = s"Upgrade all existing towers\n${gameObject.upgradePrice}" + "$"
          displayableScene.content += upgradeButton
          upgradeButton.relocate(50, 600)
        }
      }

    //primarystage ends here
    }
  }
}