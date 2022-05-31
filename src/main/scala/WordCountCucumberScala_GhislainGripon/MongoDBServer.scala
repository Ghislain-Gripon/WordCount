/*

mongo scala driver and affiliated class provoke a conflict with mongo spark connector, both can't coexist

package WordCountCucumberScala_GhislainGripon


import org.apache.log4j.BasicConfigurator
import org.mongodb.scala._
import org.mongodb.scala.bson.{BsonValue, Document}
import org.slf4j._
import scala.concurrent._

class MongoDBServer(_config: Configuration) extends DBServer(_config) {
  private val connectionString = s"mongodb://${config.getUsername}:${config.getPassword}@${config.host}:${config.port}/?authSource=${config.database}"
  val clientLogger: Logger = LoggerFactory.getLogger(MongoClient.getClass)
  val databaseLogger: Logger = LoggerFactory.getLogger(MongoDatabase.getClass)
  BasicConfigurator.configure()
  val mongoClient: MongoClient = MongoClient(connectionString)
  val database: MongoDatabase = mongoClient.getDatabase(config.database)

  override def execSQL(queryExp: String): List[(String, BsonValue)] = {
    val doc: Document = Document(queryExp)
    val future = Await.result(database.runCommand(doc).toFuture(), duration.Duration(15, "minutes"))
    future.toList
  }

  override def getConnection(): MongoDatabase = {
    database
  }

  override def closeConnection(): Unit = {
    mongoClient.close()
  }

  override def mapReduceResultToInsertable(result: List[(String, Int)]): String = {
    val mapReduceJsonList: String = result.map(doc => s"""{"${doc._1}" : ${doc._2}},""").mkString.dropRight(1)
    val headOfDocument : String = s""" { "_id": "${config.main_data}", "rawText": ["""
    headOfDocument + mapReduceJsonList + "]}"
    //turns the list into a string with key value pairs in json format for mongodb insertion
  }

}

object test {
  def main(args: Array[String]): Unit = {
  /*val conf = new Configuration()
  val mongo = new MongoDBServer(conf)
  /*val res = mongo.execSQL(
    s"""{ "insert" : "textData",
      |"documents": [{
      | "_id" : "TextToCount",
      | "rawText" : "${FileHandler.read(conf.main_data)}"
      |}] }""".stripMargin)*/
  val res = mongo.execSQL(""" { "find" : "textData", "filter" : { "_id" : "TextToCount" } } """)
  print(res)*/


  }
}
*/

