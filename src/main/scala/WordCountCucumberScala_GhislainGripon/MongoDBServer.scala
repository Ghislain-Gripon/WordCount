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

}

object test {
  def main(args: Array[String]): Unit = {
  val conf = new Configuration()
  val mongo = new MongoDBServer(conf)
  val res = mongo.execSQL("""{ "find" : "textData" }""")
  println(res)

  }
}
