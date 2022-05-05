package WordCountCucumberScala_GhislainGripon

class ReduceTask(WordMap: List[(String, Int)]) extends Task {
  def reduceWords(WordMaps: List[(String, Int)]): List[(String, Int)] = {
    WordMaps.groupBy(_._1).view.mapValues(_.size).toList
  }

  override def execute(): List[(String, Int)] = {
    reduceWords(WordMap)
  }
}
