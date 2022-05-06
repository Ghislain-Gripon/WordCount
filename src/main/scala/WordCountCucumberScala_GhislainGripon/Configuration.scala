package WordCountCucumberScala_GhislainGripon

import io.circe.yaml.parser
import io.circe._
import java.io.{FileNotFoundException, IOException}
import scala.io.Source.fromFile

case class FieldEmptyError(message: String) extends Exception(message)

//If no path to a configuration file is provided, default location is taped, src/data/Config.yaml
class Configuration(private var configFilePath: String = "") {
  private var _yamlString: Option[String] = None

  //The root directory from which the project is run is the project one, thus we have access to all the folder structure.
  if(configFilePath.isEmpty) {
    configFilePath = "src/data/Config.yaml"
  }

  try {
    Control.using(fromFile(configFilePath)) { source =>
      _yamlString = Some(source.mkString)
    }
  }
  catch {
    case e: FileNotFoundException => println("Couldn't find that file. " + e.getMessage)
    case e: IOException => println("Got an IOException! " + e.getMessage)
  }

  private val _configJson: Json = parser.parse(_yamlString.get).getOrElse(Json.Null)
  private val _cursor: HCursor = _configJson.hcursor
  private val _data_directory: ACursor = _cursor.downField("data").downField("directory")
  private val _test_directory: ACursor = _cursor.downField("test").downField("directory")
  private val _scala_test_directory: ACursor = _cursor.downField("test").downField("scala_test").downField("directory")
  private val _tests_to_run_filenames: List[String] = _cursor.downField("test").downField("scala_test").get[List[String]]("tests_to_run").getOrElse(List[String]())

  val version: String = _cursor.downField("version").as[String].getOrElse(throwError("version"))
  val test_data: String = _data_directory.as[String].getOrElse(throwError("data.directory")) + "/" + _cursor.downField("data").downField("test").as[String].getOrElse(throwError("data.directory.test"))
  val main_data: String = _data_directory.as[String].getOrElse(throwError("data.directory")) + "/" + _cursor.downField("data").downField("main").as[String].getOrElse(throwError("data.directory.main"))
  val thread_use_or_not: Boolean = _cursor.downField("threads").downField("used_or_not").as[Boolean].getOrElse(throwError("threads.used_or_not"))
  val max_thread_num: Int = _cursor.downField("threads").downField("maximum_number").as[Int].getOrElse(throwError("threads.maximum_number"))
  val gherkin: String = _test_directory.as[String].getOrElse(throwError("test.directory")) + "/" + _cursor.downField("test").downField("cucumber").downField("gherkin").as[String].getOrElse(throwError("test.cucumber.gherkin"))
  val step_definition: String = _test_directory.as[String].getOrElse(throwError("test.directory")) + "/" + _cursor.downField("test").downField("cucumber").downField("step_definition").as[String].getOrElse(throwError("test.cucumber.step_definition"))
  val tests_to_run: List[String] = _tests_to_run_filenames.map(filename => _test_directory.as[String].getOrElse(throwError("test.directory")) + "/" + _scala_test_directory.as[String].getOrElse(throwError("test.scala_test.directory")) + "/" + filename)

  private def throwError[A](field: String): A = {
    throw FieldEmptyError(s"The field at $field is empty.")
  }

  override def toString: String = _configJson.toString()

}

/*object test {
  def main(args: Array[String]): Unit = {
    val test: Json = parser.parse(
      """
        |{ test = "value" }
        |""".stripMargin).getOrElse(Json.Null)
    println(test.toString)
    test.hcursor. (parser.parse("""{ this : "worked"}""").getOrElse(Json.Null))
    println(test.toString)

  }
}*/
