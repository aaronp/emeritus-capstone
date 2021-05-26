package aaron

import aaron.Population

object NameByMSOA:
  def apply(msoa: String): String = value(msoa)

  lazy val value = Load.csv("populationByMSOA.csv").map { row =>
    row("MSOA Code") -> row("MSOA Name")
  }.toMap
