package WordCountCucumberScala_GhislainGripon
import java.io._
import scala.io.Source.fromFile

object FileHandler {
  def using[A <: {def close(): Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  def read(path: String): String = {
    var file: String = ""
    try {
      using(fromFile(path)) { source =>
        file = source.mkString
      }
    }
    catch {
      case e: FileNotFoundException => println("Couldn't find that file. " + e.getMessage)
      case e: IOException => println("Got an IOException! " + e.getMessage)
    }
    file
  }

  def write(path: String, data: String): Unit = {
    try {
      using(new File(path)) { source =>
        val bw = new BufferedWriter(new FileWriter(source))
        bw.write(data)
        bw.close()
      }
    }
    catch {
      case e: FileNotFoundException => println("Couldn't find that file. " + e.getMessage)
      case e: IOException => println("Got an IOException! " + e.getMessage)
    }
  }
}
