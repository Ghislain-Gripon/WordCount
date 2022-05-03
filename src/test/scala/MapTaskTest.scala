import WordCountCucumberScala_GhislainGripon.MapTask
import org.scalatest.wordspec.AnyWordSpec

class MapTaskTest extends AnyWordSpec {
  val data = "This is a test string. C'est une chaine de charactères de test. Devons nous ? La victoire : (En avant !)"
  val mapTask = new MapTask(data)
  val mapResult: List[(String, Int)] = mapTask.execute()

  "A Mapping WordCountCucumberScala_GhislainGripon.Task" should {
    "give back a list of type tuple string int" in {
      assert(mapResult.isInstanceOf[List[(String, Int)]])
    }

    "give words as keys" in {
      assert(!mapResult.exists(_._1.contains(Seq(',', '?', '.', ';', ':', '\'', ')', '(', '!'))))
    }

    "give 1 as values" in {
      assert(!mapResult.exists(_._2 != 1))
    }


    "not be empty" in {
      assert(mapResult.nonEmpty)
    }
  }
}

