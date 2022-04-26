class MapReduceTask[A](Data: A, MappingFunction: () => List[ValueCount[A]]) extends Task {
  override def execute(): List[ValueCount[A]] = {

    return
  }
}
