name := "capstone-aaron"

//scalaVersion := "2.12.13"
scalaVersion := "3.0.0"

val circeVersion      = "0.14.0-M7"

libraryDependencies ++= List("circe-core", "circe-generic", "circe-parser").map(artifact => "io.circe" %% artifact % circeVersion)

def spark = List("org.apache.spark" %% "spark-core" % "3.1.1",
  "org.apache.spark" %% "spark-mllib" % "3.1.1"// % "provided"
)

def sttp = "com.softwaremill.sttp.client3" %% "core" % "3.3.4" % "compile->compile;test->test"
def zio = "dev.zio" %% "zio" % "1.0.8"

def ployly = "co.theasi" %% "plotly" % "0.2.0"

def plot = ("io.github.pityka" %% "nspl-scalatags-jvm" % "0.0.24").cross(CrossVersion.for3Use2_13)

def client = ("com.typesafe.akka" %% "akka-http" % "10.2.4").cross(CrossVersion.for3Use2_13)
libraryDependencies ++= List(
  sttp,
  zio,
  plot,
  client
)
