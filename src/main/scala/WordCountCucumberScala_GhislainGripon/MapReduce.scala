package WordCountCucumberScala_GhislainGripon

import WordCountCucumberScala_GhislainGripon.Control.using

import java.io.{FileNotFoundException, IOException}

object MapReduce {
  def main(args: Array[String]): Unit = {
    var result: List[(String, Int)] = null
    try {
      val config = new Configuration()
      using(scala.io.Source.fromFile(config.main_data)) { source => {
        val mapReduceTask = new MapReduceTask(source.mkString)
        result = mapReduceTask.execute()
      }
        result.sortBy(_._2)(Ordering.Int.reverse).foreach(println)
        //Sorting the result to get the words with the most occurrences first.
      }
    } catch {
      case e: FileNotFoundException => println("Couldn't find that file. " + e.getMessage)
      case e: IOException => println("Got an IOException! " + e.getMessage)
    }
    //Control structure that enables auto disposal and closure of resources upon leaving the control block so
    //that files get closed automatically
  }
}
