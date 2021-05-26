package object aaron {

  private val QuotedLine = """(.*?)"(.*?)"(.*)""".r

  def fixCsvLine(line: String): String = {
    line match {
      case QuotedLine(before, pop, after) =>
        val fixed = pop.filterNot(_ == ',')
        s"$before$fixed${fixCsvLine(after)}"
      case text => text
    }
  }
}
