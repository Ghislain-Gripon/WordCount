package WordCountCucumberScala_GhislainGripon

import org.mongodb.scala.bson.BsonValue

object MapReduce {
  def main(args: Array[String]): Unit = {
    val config = new Configuration()
    val db: DBServer = new MongoDBServer(config)
    val query_response: List[(String, BsonValue)] = db.execSQL(config.find_command.format(config.text_table, config.find_filter.format(config.main_data))).asInstanceOf[List[(String, BsonValue)]]
    val text: String = query_response.head._2.asDocument().get("firstBatch").asArray().get(0).asDocument().get("rawText").toString
    val mapReduceTask: MapReduceTask = new MapReduceTask(text)
    val result: List[(String, Int)] = mapReduceTask.execute().sortBy(doc => doc._2)(Ordering.Int.reverse)
    val resultInJsonString: String = mapReduceTask.mapReduceResultToJson(result, config.main_data)
    println(resultInJsonString)
    val write_response: List[(String, BsonValue)] = db.execSQL(config.insert_command.format(config.result_table, resultInJsonString)).asInstanceOf[List[(String, BsonValue)]]
    println(write_response)
    val writeError = write_response.find(doc => doc._1 == "writeErrors")
    if(writeError.nonEmpty)
      throw new Error("Write errors detected, number of errors : " + writeError.get._2.asArray().size() + ", " + writeError.get._2.asArray() )
  }
}
