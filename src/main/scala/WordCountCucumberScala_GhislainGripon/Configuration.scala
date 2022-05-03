package WordCountCucumberScala_GhislainGripon

import io.circe.Json
import io.circe.yaml.parser
import io.circe._
import io.circe.parser._
import cats.syntax.either._
import scala.io.Source

class Configuration(configFilePath: String) {
  var yamlString: Option[String] = None
  Control.using(Source.fromFile(configFilePath)) { source =>
    yamlString = Some(source.mkString)
  }

  val ConfigJson: Json = parser.parse(yamlString.asInstanceOf[String]).getOrElse(Json.Null)

  def getField(field_path: String): Any = {
    ConfigJson
  }

}
