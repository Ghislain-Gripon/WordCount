package WordCountCucumberScala_GhislainGripon

import io.circe.Json
import io.circe.yaml.parser
import io.circe._
import cats.syntax.either._
import scala.io.Source
import scala.tools.nsc.io._

//If no path to a configuration file is provided, default location is taped, src/data/Config.yaml
class Configuration(var configFilePath: String = "") {
  private var _yamlString: Option[String] = None

  if(configFilePath.isEmpty) {
    configFilePath = (Path(new JFile(".").getCanonicalPath).parents(2).toString()
      + "/data/Config.yaml")
  }

  Control.using(Source.fromFile(configFilePath)) { source =>
    _yamlString = Some(source.mkString)
  }

  private val _ConfigJson: Json = parser.parse(_yamlString.asInstanceOf[String]).getOrElse(Json.Null)
  private val _cursor: HCursor = _ConfigJson.hcursor
  private val _directory: String = _cursor.downField("data").downField("directory").as[String].toString

  val version: String = _cursor.downField("version").as[String].toString
  val test_directory: String = _directory + "/" + _cursor.downField("data").downField("test").as[String].toString
  val main_directory: String = _directory + "/" + _cursor.downField("data").downField("main").as[String].toString
  val thread_use_or_not: Boolean = _cursor.downField("threads").downField("used_or_not").as[Boolean].getOrElse[Boolean]
  val max_thread_num: Int = _cursor.downField("threads").downField("maximum_number").as[Int].asInstanceOf[Int]

}
