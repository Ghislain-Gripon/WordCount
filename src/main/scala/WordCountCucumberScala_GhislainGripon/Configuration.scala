package WordCountCucumberScala_GhislainGripon

import io.circe._
import cats.syntax.either._
import io.circe.generic.auto._
import io.circe.yaml


//If no path to a configuration file is provided, default location is taped, src/data/Config.yaml
class Configuration(var configFilePath: String = "") {
  private var _yamlString: Option[String] = None

  //The root directory from which the project is run is the project one, thus we have access to all the folder structure.
  if(configFilePath.isEmpty) {
    configFilePath = "src/data/Config.yaml"
  }

  _yamlString = Some(FileHandler.read(configFilePath))

  private case class Data(
                 test: String,
                 main: String
                 )

  private case class Scala_Test(
                       directory: String,
                       tests_to_run: List[String]
                       )

  private case class Cucumber(
                     gherkin: String,
                     step_definition: String
                     )

  private case class Test(
                 directory: String,
                 scala_test: Scala_Test,
                 cucumber: Cucumber
                 )

  private case class Threads(
                    used_or_not: Boolean,
                    maximum_number: Int
                    )

  private case class FindCommand(
                         command: String,
                         filter: String
                         )

  private case class InsertCommand(
                           command: String
                           )

  private case class Dbserver(
                     engine: String,
                     port: Int,
                     host: String,
                     database: String,
                     text_table: String,
                     result_table: String,
                     find: FindCommand,
                     insert: InsertCommand
                     )

  private case class Config(
                   _id: String,
                   version: String,
                   data: Data,
                   test: Test,
                   threads: Threads,
                   dbserver: Dbserver
                   )

  private val _jsonString = yaml.parser.parse(_yamlString.get)
  private val _config = _jsonString
    .leftMap(err => err: Error)
    .flatMap(_.as[Config])
    .valueOr(throw _)

  val id: String = _config._id
  val version: String = _config.version
  val test_data: String = _config.data.test
  val main_data: String = _config.data.main
  val thread_use_or_not: Boolean = _config.threads.used_or_not
  val max_thread_num: Int = _config.threads.maximum_number
  val gherkin: String = _config.test.cucumber.gherkin
  val step_definition: String = _config.test.cucumber.step_definition
  val tests_to_run: List[String] = _config.test.scala_test.tests_to_run.map(filename => _config.test.directory + "/" +
    _config.test.scala_test.directory + "/" + filename)
  val engine: String = _config.dbserver.engine
  val port: Int = _config.dbserver.port
  val host: String = _config.dbserver.host
  val database: String = _config.dbserver.database
  val text_table: String = _config.dbserver.text_table
  val result_table: String = _config.dbserver.result_table
  val find_command: String = _config.dbserver.find.command
  val find_filter: String = _config.dbserver.find.filter
  val insert_command: String = _config.dbserver.insert.command

  private def getCredentials: List[String] = {
    var result: List[String] = List()
    if(engine == "mongodb") {
      case class Credentials(username: String, password: String)

      val credentialsFile = yaml.parser.parse(FileHandler.read("src/data/credentials/mongodb.yaml"))
      val credentials = credentialsFile
        .leftMap(err => err: Error)
        .flatMap(_.as[Credentials])
        .valueOr(throw _)
      result = List(credentials.username, credentials.password)
    }
    result
  }

  def getUsername: String = {
    getCredentials.head
  }

  def getPassword: String = {
    getCredentials(1)
  }

}

/*object test {
  def main(args: Array[String]): Unit = {
    val test = new Configuration()
    println(test.foo.test.scala_test.tests_to_run)


  }
}*/
