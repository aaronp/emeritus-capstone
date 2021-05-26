package aaron

import java.nio.file.{Files, Path, Paths}

import io.circe.Json
import sttp.client3.{Identity, Request, Response}

import java.nio.file.{Files, Path, Paths}

/**
 *
 * Write-up:
 * https://app.gitbook.com/@aaron-pritzlaff/s/capstone/~/drafts/-MaURVws6C_DRdhBdt01/
 *
 * Hospital deaths:
 * https://www.ons.gov.uk/peoplepopulationandcommunity/birthsdeathsandmarriages/deaths/adhocs/12996rollingannualdeathregistrationsbyplaceofoccurrenceenglandperiodendingquarter3octtodecoffinancialyear2020to2021
 *
 *
 * Hospital data:
 * https://data.gov.uk/dataset/f4420d1c-043a-42bc-afbc-4c0f7d3f1620/hospitals
 *
 * Population data:
 * https://www.ons.gov.uk/peoplepopulationandcommunity/populationandmigration/populationestimates/datasets/populationestimatesforukenglandandwalesscotlandandnorthernireland
 * "Mid-2019: April 2019 local authority district codes"
 *
 * Bed Occupancy:
 * https://www.england.nhs.uk/statistics/statistical-work-areas/bed-availability-and-occupancy/bed-data-overnight/
 *
 * https://ourworldindata.org/coronavirus
 *
 * What we want to find out:
 *
 * {{{
 *   * Population per Capacity
 *   * Covid Cases by population density
 *   * Covid Cases by poverty index
 *   * Covid Deaths by population density
 *   * Largest Increase in Deaths by location
 * }}}
 *
 * Things we could use a linear problem on:
 * {{{
 *
 *   decision variables:
 *     H1 = num beds to bring up to ave
 *     ...
 *     Hn = num beds to bring up to ave
 *
 *   objective function: minimize deaths
 *
 *   constraints : cost per bed
 *                 max beds
 *
 * }}}
 *
 *
 *
 */
object Hospitals {

  def main(args: Array[String]) = {
    val Hartlepool = latLongForCity("Hartlepool")
    println(Hartlepool.spaces4)
  }

  def latLongForCity(name: String) = {
    import sttp.client3.quick._
    val response: Response[String] = quickRequest.get(uri"https://maps.googleapis.com/maps/api/geocode/json?key=$apiKey&address=$name").send(backend)
    val Right(jason) = io.circe.parser.parse(response.body)
    val status = jason.hcursor.downField("status").as[String]
    val x = jason.hcursor.downField("results").as[List[Json]]
    jason
  }


  def parseLocation(tsv: String) = tsv.split(";", -1)

  val apiKey = "AIzaSyBRVzLVVh25J2HuvlLl-17DUd2MTtHam8o"

  case class Row(cells:   Map[String, String]) {
    override def toString: String = cells.keys.toList.sorted.map { k =>
      s"$k : ${cells(k)}"
    }.mkString("\n")

    def lat = cells("Latitude")

    def long = cells("Longitude")

    def hasLatLong = lat.nonEmpty && long.nonEmpty
  }

  def parseLocations = {

    val path: Path = {
      val uri = getClass.getClassLoader.getResource("locations.tsv").toURI
      Paths.get(uri)
    }
    val allLines: List[String] = Files.readString(path).linesIterator.toList
    val headLine :: tail = allLines

    val head = headLine.split(";", -1)

    val rows = tail.zipWithIndex.map {
      case (line, lineNr) =>
        val cells = parseLocation(line)

        if (cells.size != head.size) {
          println(s"Row ${lineNr + 1} has ${cells.size} cells != ${head.size}: $line")
        }

        lineNr -> Row(head.zip(cells).toMap)
    }.toMap

    val missing = rows.collect {
      case (nr, row) if !row.hasLatLong => (nr, row)
    }

    // Cambridge University Hospital missing latlong is 52.17485,0.14081

    println(s"${missing.size} missing: ")
    missing.foreach { r =>
      println(s"-" * 120)
      println(r)
    }
  }
}