package aaron

object DeathsByMSOA {

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

  def main(array: Array[String]) = {

    val months = "March,April,May,June,July,August,September,October,November,December".split(",")

    // don't underestimate linear regression models
    def densityCorrelation = months.zipWithIndex.map {
      case (month, idx) =>
        val points = deathsByDensity(month).map(_._2)
        s"$month,${idx + 3},${LinearRegression.correlationCoefficient(points.toList)}"
    }.mkString("death to population correlation coefficient:", "\n", "")

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


    // TODO - need to normalize the deaths across months

//    println(densityCorrelation)
//    println(populationCorrelation)
    println(areaCorrelation)

  }

}
