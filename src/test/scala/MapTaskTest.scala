import org.scalatest.wordspec.AnyWordSpec

class MapTaskTest extends AnyWordSpec {
  val data = "This is a test string. C'est une chaine de charactères de test. Devons nous ? La victoire : (En avant !)"
  val mapTask = new MapTask(data)
  val mapResult: List[(String, Int)] = mapTask.execute()

  "A Mapping Task" should {
    "give back a list of type tuple string int" in {
      assert(mapResult.isInstanceOf[List[(String, Int)]])
    }

    "give words as keys and 1 as values" in {
      assert(!mapResult.exists(_._1.contains(Seq(',', '?', '.', ';', ':', '\'', ')', '(', '!'))))
      assert(!mapResult.exists(_._2 != 1))
    }

    "not be empty" in {
      assert(mapResult.nonEmpty)
    }
  }
}
