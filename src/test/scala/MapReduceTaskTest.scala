import WordCountCucumberScala_GhislainGripon.MapReduceTask
import org.scalatest.wordspec.AnyWordSpec

class MapReduceTaskTest extends AnyWordSpec {
  val reduceTaskTest = new ReduceTaskTest
  val mapTaskTest = new MapTaskTest
  val data = "This is a test string. C'est une chaine de charactères de test. Devons nous ? La victoire : (En avant !)"
  val mapReduceTask = new MapReduceTask(data)
  val mapReduceResult: List[(String, Int)] = mapReduceTask.execute()

  "A Map Reduce WordCountCucumberScala_GhislainGripon.Task" should {
    "succeed both the tests of its subtasks" in {
      mapTaskTest.execute()
      reduceTaskTest.execute()
    }

    "give back a list of type tuple string int" in {
      assert(mapReduceResult.isInstanceOf[List[(String, Int)]])
    }

    "give words as keys without punctuation" in {
      assert(!mapReduceResult.exists(_._1.contains(Seq(',', '?', '.', ';', ':', '\'', ')', '(', '!'))))
    }

    "give values superior or equal to 1" in
      {
        assert(!mapReduceResult.exists(_._2 < 1))
      }

    "not be empty" in {
      assert(mapReduceResult.nonEmpty)
    }

    "not contain duplicate keys" in {
      assert(mapReduceResult.distinct.size == mapReduceResult.size)
    }
  }



}
