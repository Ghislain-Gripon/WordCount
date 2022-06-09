package WordCountCucumberScala_GhislainGripon

import io.circe._
import cats.syntax.either._
import io.circe.generic.auto._
import io.circe.yaml


//If no path to a configuration file is provided, default location is taped, src/data/Config.yaml
class Configuration(var configFilePath: String = "") extends Serializable {
  private var _yamlString: Option[String] = None

  //The root directory from which the project is run is the project one, thus we have access to all the folder structure.
  if(configFilePath.isEmpty) {
    configFilePath = "src/data/Config.yaml"
  }

  _yamlString = Some(FileHandler.read(configFilePath))

  private case class Data(
                           directory: String,
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
                     text_field: String,
                     find: FindCommand,
                     insert: InsertCommand,
                     spark_data_source: String
                     )

  private case class Spark(
                            thread_num: Int,
                           write_mode: String
                          )

  case class KafkaNode(
                              id: String,
                              port: Int
                              ) extends Serializable

  private case class Kafka(
                            docker_compose_path: String,
                            zookeeper_port: Int,
                            spark_to_mongo_topic: String,
                            kafka_to_spark_topic: String,
                            kafka_nodes: List[KafkaNode]
                          )

  private case class Avro(
                         schema_file: String
                         )

  private case class Config(
                   _id: String,
                   version: String,
                   data: Data,
                   test: Test,
                   threads: Threads,
                   dbserver: Dbserver,
                   spark: Spark,
                   kafka: Kafka,
                   avro: Avro
                   )

  private val _jsonString = yaml.parser.parse(_yamlString.get)
  private val _config = _jsonString
    .leftMap(err => err: Error)
    .flatMap(_.as[Config])
    .valueOr(throw _)

  val id: String = _config._id
  val version: String = _config.version
  val data_dir: String = _config.data.directory
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
  val text_field: String = _config.dbserver.text_field
  val find_command: String = _config.dbserver.find.command
  val find_filter: String = _config.dbserver.find.filter
  val insert_command: String = _config.dbserver.insert.command
  val engine_spark_data_source: String = _config.dbserver.spark_data_source
  val spark_threads: Int = _config.spark.thread_num
  val spark_write_mode: String = _config.spark.write_mode
  val kafka_zookeeper_port: Int = _config.kafka.zookeeper_port
  val kafka_dockercompose_file: String = _config.kafka.docker_compose_path
  val kafka_nodes: List[KafkaNode] = _config.kafka.kafka_nodes
  val avro_schema: String = _config.avro.schema_file
  val spark_to_mongo_topic: String = _config.kafka.spark_to_mongo_topic
  val kafka_to_spark_topic: String = _config.kafka.kafka_to_spark_topic

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
