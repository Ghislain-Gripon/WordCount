class MapReduceTask(Data: String) extends Task {
  override def execute(): List[(String, Int)] = {
    val mappingTask = new MapTask(Data)
    val mapping = mappingTask.execute()
    val reducingTask = new ReduceTask(mapping)
    val reducedMapping = reducingTask.execute()
    reducedMapping
  }
}
