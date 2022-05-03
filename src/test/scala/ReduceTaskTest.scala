import WordCountCucumberScala_GhislainGripon.{MapTask, ReduceTask}
import org.scalatest.wordspec.AnyWordSpec

class ReduceTaskTest extends AnyWordSpec {
  val data = "This is a test string. C'est une chaine de charactères de test. Devons nous ? La victoire : (En avant !)"
  val mapTask = new MapTask(data)
  val mapResult: List[(String, Int)] = mapTask.execute()
  val reduceTask = new ReduceTask(mapResult)
  val reduceResult: List[(String, Int)] = reduceTask.execute()

  "A Reducing WordCountCucumberScala_GhislainGripon.Task" should {
    "give back a list of type tuple string int" in {
      assert(reduceResult.isInstanceOf[List[(String, Int)]])
    }

    "give words as keys without punctuation" in {
      assert(!reduceResult.exists(_._1.contains(Seq(',', '?', '.', ';', ':', '\'', ')', '(', '!'))))
    }

    "give values superior or equal to 1" in
    {
      assert(!reduceResult.exists(_._2 < 1))
    }

    "not be empty" in {
      assert(reduceResult.nonEmpty)
    }

    "not contain duplicate keys" in {
      assert(reduceResult.distinct.size == reduceResult.size)
    }
  }
}
