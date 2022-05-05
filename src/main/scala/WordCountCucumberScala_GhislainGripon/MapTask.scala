package WordCountCucumberScala_GhislainGripon

import scala.util.matching.Regex

class MapTask(Data: String) extends Task {
  def mapWords(Text: String): List[(String, Int)] = {
    val wordSplitting: Regex = "(((?U)\\w)+)".r
    val words = wordSplitting.findAllIn(Text)
    words.toList.map(word => (word, 1))
  }

  override def execute(): List[(String, Int)] = {
    mapWords(Data)
  }
}
