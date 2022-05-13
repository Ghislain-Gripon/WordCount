package WordCountCucumberScala_GhislainGripon

abstract class DBServer(_config: Configuration) {
  val config: Configuration = _config
  def execSQL(queryExp: String): Any
  def getConnection(): Any
  def closeConnection(): Unit
  def mapReduceResultToInsertable(result: List[(String, Int)]) : Any
}
