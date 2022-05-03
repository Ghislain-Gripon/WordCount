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
}
