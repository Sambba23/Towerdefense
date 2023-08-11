package All.Game

import javafx.animation.AnimationTimer

class Ticker(function: () => Unit) extends AnimationTimer {

  //Override from animation timer
  override def handle(now: Long): Unit = {function()}

}
