import WordCountCucumberScala_GhislainGripon.{Configuration, FileHandler, MapTask}
import org.scalatest.wordspec.AnyWordSpec

import java.io.{FileNotFoundException, IOException}
import scala.io.Source.fromFile

class MapTaskTest extends AnyWordSpec {

  var data: String = ""
  try {
    FileHandler.using(fromFile(new Configuration().test_data)) { source =>
      data = source.mkString
    }
  }
  catch {
    case e: FileNotFoundException => println("Couldn't find that file. " + e.getMessage)
    case e: IOException => println("Got an IOException! " + e.getMessage)
  }
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

