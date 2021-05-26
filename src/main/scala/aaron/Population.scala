package aaron

object Population {
  def densityByMsoa(msoa: String): Double = densityByMsoaMap.getOrElse(msoa, sys.error(s"Density Couldn't find MSOA '$msoa'"))

  def byMsoa(msoa: String): Double = populationByMSOA.getOrElse(msoa, sys.error(s"Population Couldn't find MSOA '$msoa'"))

  def areaMyMsoa(msoa: String): Double = areaByMsoaMap.getOrElse(msoa, sys.error(s"Area Couldn't find MSOA '$msoa'"))

  private lazy val populationByMSOA: Map[String, Int] = {
    val populationByMSOARows = Load.csv("populationByMSOA.csv").map { row =>
      row("MSOA Code") -> row("All Ages").toInt
    }
    populationByMSOARows.toMap.ensuring(_.size == populationByMSOARows.size)
  }

  private lazy val areaByMsoaMap = {
    val boundaries = Load.csv("MSOABoundaries.csv")
    boundaries.map { row =>
      val msoa = row("msoa11cd")
      val area = row("st_areashape").toDouble
      msoa -> area
    }.toMap.ensuring(_.size == boundaries.size)
  }

  private lazy val densityByMsoaMap: Map[String, Double] = {
    areaByMsoaMap.map {
      case (msoa, area) =>
        val population: Int = populationByMSOA(msoa)
        val density = population / area.ensuring(_ != 0)
        msoa -> density
    }
  }

}
