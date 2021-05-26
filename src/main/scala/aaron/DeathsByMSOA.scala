package aaron

/**
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
 */
object DeathsByMSOA:

  def main(array: Array[String]) =
    println(densityCorrelation)
    println(populationCorrelation)
    println(areaCorrelation)

  lazy val deathsByMsoa = Load.csv("deathsByMSOA.csv")

  private def plot(month: String)(calc: String => Double) = deathsByMsoa.map { row =>
    val msoa = row("MSOA code")
    require(!msoa.isEmpty, s"Invalid code in deathsByMSOA.csv: $row")
    val density = calc(msoa)
    val deaths = row(month).toInt
    val name = NameByMSOA(msoa)
    name -> LinearRegression.Point(density, deaths)
  }

  def deathsByDensity(month: String) = plot(month)(Population.densityByMsoa)

  def deathsByPopulation(month: String) = plot(month)(Population.byMsoa)

  def deathsByArea(month: String) = plot(month)(Population.areaMyMsoa)


  val months = "March,April,May,June,July,August,September,October,November,December".split(",")

  // don't underestimate linear regression models
  def densityCorrelation = months.zipWithIndex.map {
    case (month, idx) =>
      val points = deathsByDensity(month).map(_._2)
      s"$month,${idx + 3},${LinearRegression.correlationCoefficient(points.toList)}"
  }.mkString("death to population density correlation coefficient:", "\n", "")

  def populationCorrelation = months.zipWithIndex.map {
    case (month, idx) =>
      val points = deathsByPopulation(month).map(_._2)
      s"$month,${idx + 3},${LinearRegression.correlationCoefficient(points.toList)}"
  }.mkString("death to population correlation coefficient:", "\n", "")

  def areaCorrelation = months.zipWithIndex.map {
    case (month, idx) =>
      val points = deathsByArea(month).map(_._2)
      s"$month,${idx + 3},${LinearRegression.correlationCoefficient(points.toList)}"
  }.mkString("death to area correlation coefficient:", "\n", "")

end DeathsByMSOA