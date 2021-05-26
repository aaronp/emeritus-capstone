package aaron

import io.circe.Json
import sttp.client3.{Identity, Request, Response}

import java.net.{HttpURLConnection, URL}
import java.nio.file.{Files, Path, Paths}
import scala.io.Source

object MSOA {

  def main(args: Array[String]) = {
    val a = stAreaFor("msoa11cd='E02000019'")
    val E02000364 = stAreaFor("msoa11cd='E02000364'")
    println("...")
    println(a)
    println("--")
    println(E02000364)
    println("--")
  }

  def stAreaFor(msoa: String) = {

    //https://geoportal.statistics.gov.uk/datasets/826dc85fb600440889480f4d9dbb1a24_3?geometry=-18.389%2C50.522%2C14.043%2C55.161

    val cmd = s"https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=msoa11cd%20%3D%20'$msoa'&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json"
    val cmd2 = s"https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=msoa11cd%20%3D%20msoa11cd='E02000364'&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json"
  }
  def stAreaForJava(msoa: String) = {
    import sttp.client3.quick._
//    val wholeThing = "https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=msoa11cd%20%3D%20'E02000004'&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json"
    val ffs = "msoa11cd = 'E02000004'"


    val url = new URL(s"https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=msoa11cd%20%3D%20'$msoa'&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json")
    val con = url.openConnection.asInstanceOf[HttpURLConnection]
    con.setRequestMethod("GET")
    con.setRequestProperty("Content-Type", "application/json")
    import java.io.DataOutputStream
    con.setDoOutput(true)

    val out = new DataOutputStream(con.getOutputStream)
    //out.writeBytes(ParameterStringBuilder.getParamsString(parameters))
    out.flush()
    out.close()

    val src = Source.fromInputStream(con.getInputStream)
    val result = src.getLines().mkString("\n")
    println(result)
    result
  }

  def stAreaForFFS(msoa: String) = {
    import sttp.client3.quick._
//    val wholeThing = "https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=msoa11cd%20%3D%20'E02000004'&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json"
    val ffs = "msoa11cd = 'E02000004'"
    val wholeThing = s"https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=$ffs&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json"
    val in : sttp.model.Uri = uri"https://ons-inspire.esriuk.com/arcgis/rest/services/Census_Boundaries/Middle_Super_Output_Areas_December_2011_Boundaries/MapServer/3/query?where=$ffs&outFields=st_area(shape)&returnGeometry=false&orderByFields=st_area(shape)&outSR=&f=json"
    println(in)
    //    val response: Response[String] = quickRequest.get().send(backend)
    val response: Response[String] = quickRequest.get(in).send(backend)
    val Right(jason) = io.circe.parser.parse(response.body)
    jason
  }

}
