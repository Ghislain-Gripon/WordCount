import WordCountCucumberScala_GhislainGripon._
import org.scalatest.wordspec.AnyWordSpec
import java.io.{FileNotFoundException, IOException}
import scala.io.Source.fromFile

class MapReduceTaskTest extends AnyWordSpec {
  val reduceTaskTest = new ReduceTaskTest
  val mapTaskTest = new MapTaskTest
  val data: String = FileHandler.read("src/data/TestData.txt")
  val mapReduceTask = new MapReduceTask(data)
  val mapReduceResult: List[(String, Int)] = mapReduceTask.execute()

  "A MapReduceTask" should {
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

    "give values superior or equal to 1" in {
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
