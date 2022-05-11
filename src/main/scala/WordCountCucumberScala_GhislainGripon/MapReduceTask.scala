package WordCountCucumberScala_GhislainGripon

class MapReduceTask(Data: String) extends Task {
  override def execute(): List[(String, Int)] = {
    val mappingTask = new MapTask(Data)
    val mapping = mappingTask.execute()
    //Maps words to (key, 1)
    val reducingTask = new ReduceTask(mapping)
    val reducedMapping = reducingTask.execute()
    //Reduces the (key, value) tuples.
    //Ideally this is to be used with threads to optimise speed and memory usage with the transition stage reducing tasks
    reducedMapping
  }

  def mapReduceResultToJson(result: List[(String, Int)], resultId: String): String = {
    val mapReduceJsonList: String = result.map(doc => s"""{"${doc._1}" : ${doc._2}},""").mkString.dropRight(1)
    val headOfDocument : String = s""" { "_id": "$resultId", "rawText": ["""
    headOfDocument + mapReduceJsonList + "]}"
    //turns the list into a string with key value pairs in json format for mongodb insertion
  }
}
