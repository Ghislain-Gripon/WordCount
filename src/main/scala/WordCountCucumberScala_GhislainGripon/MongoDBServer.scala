package WordCountCucumberScala_GhislainGripon
import org.mongodb.scala._
import io.circe.parser

class MongoDBServer(_config: Configuration) extends DBServer(_config) {
  private val connectionString = s"mongodb://${config.getUsername}:${config.getPassword}@${config.host}:${config.port}/?authSource=${config.database}"
  val mongoClient: MongoClient = MongoClient(connectionString)
  val database: MongoDatabase = getConnection()

  override def execSQL(queryExp: Document): Any = {

  }

  override def getConnection(): MongoDatabase = {
    mongoClient.getDatabase(config.database)
  }

  override def closeConnection(): Unit = {
    mongoClient.close()
  }

}
