package aaron

import aaron.DeathsByMSOA.getClass

import java.net.URL
import scala.io.Source

object Load {

  def csv(name: String): LazyList[Map[String, String]] = {
    val cols = source(name).getLines().map { line =>
      fixCsvLine(line).split(",", -1)
    }.to(LazyList)

    val head = cols.head
    cols.tail.zipWithIndex.map {
      case (row, i) =>
        head.ensuring(_.size == row.size, s"$name: row ${i + 1} cols ${head.size} != ${row.size}").zip(row).toMap
    }
  }

  def source(name: String) = Source.fromURL(url(name))

  def url(name: String): URL = {
    getClass.getClassLoader.getResource(name)
  }
}
