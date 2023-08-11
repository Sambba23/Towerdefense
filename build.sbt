ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "cloontdbattles"
  )

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24"
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"  case _ => throw new Exception("Unknown platform!")
}
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map( m =>  "org.openjfx" % s"javafx-$m" % "14.0.1" classifier osName
)
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
 libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.8.1"
libraryDependencies +="com.lihaoyi" %% "upickle" % "2.0.0"